from socket import *
import datetime

serverPort = 9000  #
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(("", serverPort))
serverSocket.listen(1)
print("the server is ready to receive")

while True:
    connectionSocket, address = serverSocket.accept()
    sentence = connectionSocket.recv(2048).decode()
    print(address)
    print(sentence)
    # The ip and the port of the client
    ip = address[0]
    port = address[1]
    headers = sentence.split('\n')
    print(headers)  # ['GET /file.css HTTP/1.1\r', 'Host: localhost:6500\r'....
    file = headers[0].split()[1]

    if file == '/' or file == '/en':  # When requesting / or /en
        file = 'main_en.html'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        # Sending the server the content type and the read file
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8")) # type text/html
        print("Content-Type: text/html\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print(x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/main2_en.html?':  # When requesting /main2_en.html?
        file = 'main2_en.html'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8"))
        print("Content-Type: text/html\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print(x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/ar':  # When requesting /ar
        file = 'main_ar.html'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8"))
        print("Content-Type: text/html\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print(x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/main2_ar.html?':  # When requesting /main2_ar.html?
        file = 'main2_ar.html'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8"))
        print("Content-Type: text/html\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print(x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/design.css':  # when requesting /design.css
        file = 'design.css'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/css\r\n", "UTF-8"))
        print("Content-Type: text/css\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/design2.css':  # when requesting /design2.css
        file = 'design2.css'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/css\r\n", "UTF-8"))
        print("Content-Type: text/css\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/Errorpage.css':  # when requesting /Errorpage.css
        file = 'Errorpage.css'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/css\r\n", "UTF-8"))  # type text/css
        print("Content-Type: text/css\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/tala.png':  # when requesting /tala.png
        fin = open("tala.png", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/png\r\n\r\n", "UTF-8"))  # type image/png
        print("Content-Type: image/png\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/mazen.png':  # when requesting /mazen.png
        fin = open("mazen.png", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/png\r\n\r\n", "UTF-8"))
        print("Content-Type: image/png\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/rania.png':  # when requesting /rania.png
        fin = open("rania.png", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/png\r\n\r\n", "UTF-8"))
        print("Content-Type: image/png\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/pur.jpg':  # when requesting /pur.jpg
        fin = open("pur.jpg", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/jpeg\r\n\r\n", "UTF-8"))  # type image/jpeg
        print("Content-Type: image/jpeg\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/g.gif':  # when requesting /g.gif // for the error page
        fin = open("g.gif", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/gif\r\n\r\n", "UTF-8"))
        print("Content-Type: image/gif\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/notf.gif':  # when requesting /notf.gif // for the error page
        fin = open("notf.gif", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/gif\r\n\r\n", "UTF-8"))
        print("Content-Type: image/gif\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/design2_ar.css':  # when requesting /design2_ar.css
        file = 'design2_ar.css'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/css\r\n", "UTF-8"))
        print("Content-Type: text/css\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file == '/go':  # When requesting /go
        connectionSocket.send("HTTP/1.1 307 Temporary_Redirect\r\n".encode("UTF-8"))  # Type of redirect
        print("HTTP/1.1 307 Temporary_Redirect\r\n")
        connectionSocket.send("Location: https://www.google.com/\n".encode("UTF-8"))  # Destination
        print("Location: https://www.google.com/\n")
        connectionSocket.send("Content-Type: text/html; charset=UTF-8".encode("UTF-8"))  # type text/html
        print("Content-Type: text/html; charset=UTF-8")
        connectionSocket.send("\r\n".encode("UTF-8"))
        connectionSocket.close()

    elif file == '/cn':
        connectionSocket.send("HTTP/1.1 307 Temporary_Redirect\r\n".encode("UTF-8"))
        print("HTTP/1.1 307 Temporary_Redirect\r\n")
        connectionSocket.send("Location: https://edition.cnn.com/\n".encode("UTF-8"))
        print("Location: https://edition.cnn.com/\n")
        connectionSocket.send("Content-Type: text/html; charset=UTF-8".encode("UTF-8"))
        print("Content-Type: text/html; charset=UTF-8")
        connectionSocket.send("\r\n".encode("UTF-8"))
        connectionSocket.close()

    elif file == '/bzu':
        connectionSocket.send("HTTP/1.1 307 Temporary_Redirect\r\n".encode("UTF-8"))
        print("HTTP/1.1 307 Temporary_Redirect\r\n")
        connectionSocket.send("Location: https://www.birzeit.edu/\n".encode("UTF-8"))
        print("Location: https://www.birzeit.edu/\n")
        connectionSocket.send("Content-Type: text/html; charset=UTF-8".encode("UTF-8"))
        print("Content-Type: text/html; charset=UTF-8")
        connectionSocket.send("\r\n".encode("UTF-8"))
        connectionSocket.close()

    elif file.endswith('.html'):  # A request that ends with .html
        file = 'main_ar.html'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8"))
        print("Content-Type: text/html\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print(x.strftime("%c"))  # print the date and time in the terminal

    elif file.endswith('.jpg'):  # A request that ends with .jpg
        fin = open("pur.jpg", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/jpeg\r\n\r\n", "UTF-8"))
        print("Content-Type: image/jpeg\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file.endswith('.css'):  # A request that ends with .css
        file = 'design.css'
        fin = open(file, encoding="utf-8")
        content = fin.read()
        fin.close()
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: text/css\r\n", "UTF-8"))
        print("Content-Type: text/css\r\n")
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    elif file.endswith('.png'):  # A request that ends with .png
        fin = open("tala.png", "rb")
        connectionSocket.send(bytes("HTTP/1.1 200 OK \r\n", "UTF-8"))
        print("HTTP/1.1 200 OK \r\n")
        connectionSocket.send(bytes("Content-Type: image/png\r\n\r\n", "UTF-8"))
        print("Content-Type: image/png\r\n\r\n")
        connectionSocket.send(fin.read())  # send the contents of the picture
        print(str(fin.read()))
        x = datetime.datetime.now()  # to print the date and time
        print("Date:", x.strftime("%c"))  # print the date and time in the terminal

    else:  # Error page
        content = '<!DOCTYPE html><html><head><link rel="stylesheet" href="Errorpage.css"><title> Error</title></head>' \
                  '<body><p class="code1"><img src="notf.gif" alt="Error" /> Ooops! </p>' \
                  '<p id="msg" class="Emsg"> The file is not found </p><p id="names" class="Ename">We cant seem to ' \
                  'find page you are looking for!</p><p class="code"><img src="g.gif" class="img2" /> Error Code: ' \
                  '404</p><p id="names" class="Ename"> <strong> Rania 1190585 Tala 1191590 Mazen 1190102 ' \
                  '</strong> </p> <div class="ips">The IP is &#8594 ' + \
                  str(ip) + '</div><div class="port">The Port number is &#8594 ' + str(port) + '</div></body></html>'
        connectionSocket.send(bytes("HTTP/1.1 404 Not Found \r\n", "UTF-8"))
        print("HTTP/1.1 404 Not Found \r\n")
        connectionSocket.send(bytes("Content-Type: text/html\r\n", "UTF-8"))
        connectionSocket.send(bytes("\r\n", "UTF-8"))
        print("Content-Type: text/html\r\n")
        connectionSocket.sendall(bytes(content, "UTF-8"))
        x = datetime.datetime.now()
        print("Date:", x.strftime("%c"))
