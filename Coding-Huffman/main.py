import math
from functools import cmp_to_key

import docx2txt
from tabulate import tabulate

file_path = "To+Build+A+Fire+by+Jack+London.docx"
text = docx2txt.process(file_path)
text = text.replace('\n', '')
text = text.lower()
codes = dict()


class Node:
    def __init__(self, symbol, probability: int, left=None, right=None):
        self.symbol = symbol
        self.probability = probability
        self.left = left
        self.right = right
        self.code = ''


def get_code(char, value=''):
    temp = value + str(char.code)
    if char.left:
        get_code(char.left, temp)
    if char.right:
        get_code(char.right, temp)
    if not char.left and not char.right:
        codes[char.symbol] = temp


def result(message_to_encode):
    encoded_string = []
    for c in message_to_encode:
        encoded_string.append(codes[c])

    string = ''.join([str(item) for item in encoded_string])
    return string


def huffman(unique_symbols, frequency):
    nodes = []
    for element in unique_symbols:
        nodes.append(Node(element, int(frequency[element])))

    if len(nodes) == 1:
        nodes[0].code = 0

    while len(nodes) > 1:
        nodes = sorted(nodes, key=lambda x: x.probability)
        right = nodes[0]
        left = nodes[1]
        left.code = 0
        right.code = 1
        new_node = Node(left.symbol + right.symbol, left.probability + right.probability, left, right)
        nodes.remove(left)
        nodes.remove(right)
        nodes.append(new_node)

    get_code(nodes[0])
    return result(unique_symbols)


def compare(item1, item2):
    def order_key(item):
        if item[0].isalpha():
            return 0, item[0]
        else:
            return 1, item[0]

    return (order_key(item1) > order_key(item2)) - (order_key(item1) < order_key(item2))


unique_symbols = set(text)
NASCII = len(text) * 8
count = {}

for symbol in unique_symbols:
    count[symbol] = text.count(symbol)

table_data = []
for symbol in unique_symbols:
    if symbol != ' ':
        table_data.append([symbol, count[symbol]])

table_data = sorted(table_data, key=cmp_to_key(compare))
table_data.append(['space', count[' ']])
table_data.append(['Total', len(text)])

alphabet_entropy = 0
total_alphabets = 0

for symbol in unique_symbols:
    if 'a' <= symbol <= 'z':
        total_alphabets += count[symbol]

for symbol in unique_symbols:
    if 'a' <= symbol <= 'z':
        p = count[symbol] / total_alphabets
        alphabet_entropy -= p * math.log(p, 2)

table_data.append(['Entropy of Alphabets', str(round(alphabet_entropy, 4))])
headers = ["Symbol", "Frequency"]
print(tabulate(table_data, headers, tablefmt="grid"))

encoded_message = huffman(unique_symbols, count)
average, entropy, sum_prob, Nhuffman = 0, 0, 0, 0
symbols = ['a', 'b', 'c', 'd', 'e', 'f', 'm', 'z', ' ', '.']
table_data = []

for symbol in symbols:
    p = count[symbol] / len(text)
    length_of_codeword = len(codes[symbol])
    if symbol == ' ':
        table_data.append(['space', round(p, 4), codes[symbol], length_of_codeword])
    else:
        table_data.append([symbol, round(p, 4), codes[symbol], length_of_codeword])

headers = ["Symbol", "Probability", "Codeword", "Codeword Length"]
print(tabulate(table_data, headers, tablefmt="grid"))

for symbol in unique_symbols:
    p = count[symbol] / len(text)
    length_of_codeword = len(codes[symbol])
    Nhuffman += length_of_codeword * count[symbol]
    sum_prob += p
    average += p * len(codes[symbol])
    entropy -= p * math.log(p, 2)

print('Sum of probabilities = ' + str(round(sum_prob, 4)))
print("If ASCII code is used, the number of bits needed to encode the full story = " + str(NASCII))
print("The average number of bits/character  = " + str(round(average, 3)) + " Bits/Character")
print("The entropy = " + str(round(entropy, 2)) + " Bits/Character")
print("The total number of bits needed to encode the entire story using Huffman code = " + str(Nhuffman))
print("Compression ratio = " + str(round((Nhuffman / NASCII) * 100, 3)) + " %")
