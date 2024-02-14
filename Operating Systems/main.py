import threading
from collections import deque
from queue import Queue
from tabulate import tabulate
from threading import Thread
import threading
import time

Processes = []
MemorySize = 0
NumOfPages = 0
MinFrameSize = 0
ReadyQueue = deque()
BlockedQueue = deque()
Cycles = 0
Memory = []
MemoryCyclesHit = 1
DiskCyclesHit = 300
MemoryIndex = 0
PageFaults = 0
FIFOList = Queue()
time = 0
doneReading = False
lock = threading.Lock()
threadList = []


class Proc(Thread):
    def __init__(self, pid, begin, duration, size, trace):
        Thread.__init__(self)
        self.pid = int(pid)
        self.begin = int(begin)
        self.duration = int(duration)
        self.Remaining = int(duration)
        self.end = -1
        self.Waiting = -1
        self.TurnAroundTime = -1
        self.size = int(size)
        self.trace = trace
        self.Pages = []
        self.NumFaults = 0
        self.PageLocation = 0
        self.ended = False
        self.ThreadExit = False

    def run(self):
        lock.acquire()
        print("Starting Thread with PID ")
        lock.release()

    def stop(self):
        self.ThreadExit = True

    def setRemaining(self, new):
        self.Remaining = new

class Frame:
    def __init__(self, Pid, Pnum, RefBit):
        self.Pid = Pid
        self.Pnum = Pnum
        self.RefBit = RefBit
def finalData():
    d = []
    for process in Processes:
        array = [process.pid, process.begin, process.end, process.Waiting, process.TurnAroundTime, process.NumFaults]
        d.append(array)

    print(
        tabulate(d, headers=['ID', 'Arrival time', 'End time', 'Waiting time', 'Turnaround time', 'Number of faults']))
def cases(choice):
    global doneReading
    if choice == 1:
        if doneReading:
            menu()
            return

        print("\n\t\t  Please specify the file name:", end=" ")
        fileName = input()
        ReadFile(fileName)
        print("\n\t\t  File was read successfully")
        menu()
    elif choice == 2:
        RRthread = Thread(target=RoundRobin, args=(1,))
        RRthread.start()
        RRthread.join()
        finalData()
    elif choice == 3:
        RRthread = Thread(target=RoundRobin, args=(2,))
        RRthread.start()
        RRthread.join()
        finalData()
def menu():
    print("\n\t\t  __________________________ Virtual Memory Management Simulation _____________________________")
    print("\n\t\t  1. Read the file")
    print("\n\t\t  2. LRU")
    print("\n\t\t  3. FIFO")
    print("\n\t\t  _____________________________________________________________________________________________")
    print("\n\t\t  Enter your choice:", end=" ")
    choice = int(input())
    while choice < 1 or choice > 3:
        print("Your choice should be between 1 and 4!\n")
        menu()

    cases(choice)
def ReadFile(fileName):
    global doneReading
    doneReading = True
    file = open(fileName, "r")
    lines = file.read().strip().split("\n")
    global MemorySize, NumOfProcesses, MinFrameSize
    NumOfProcesses = int(lines[0])
    MemorySize = int(lines[1])
    MinFrameSize = int(lines[2])
    for line in lines[3:]:
        Plist = line.split(" ")
        pid = Plist[0]
        begin = Plist[1]
        duration = Plist[2]
        size = Plist[3]
        trace = Plist[4:]
        process = Proc(pid, begin, duration, size, trace)
        Processes.append(process)
        for address in trace:
            Pnum = int(address, 16) / pow(2, 12)
            frame = Frame(pid, Pnum, False)
            process.Pages.append(frame)

    print("\n\t\t  Enter the quantum time:", end=" ")
    global quantum
    quantum = int(input())



def RoundRobin(algo):
    global Memory, ReadyQueue, threadList, time , Cycles, BlockedQueue,lock
    Memory = [Frame(-1, -1, False) for i in range(MemorySize)]
    prev = None

    while not ProceessesFinished():
        lock.acquire()

        ProcessArrival()
        if len(ReadyQueue) == 0:
            time = time + 1
            if lock.locked():
                lock.release()
            continue
        p = ReadyQueue[0]

        if p.is_alive() == False and p not in threadList:
            threadList.append(p)
            p.start()


        if p.begin == -1:
            p.begin = time

        if prev != p and prev != None:
            global Cycles
            Cycles = Cycles + 5
        out = False

        for i in range(p.PageLocation, p.PageLocation + quantum):

            if i < len(p.Pages):
                check = CheckMemory(p.Pages[i],p)
                if not check:
                    ReadyQueue.popleft()
                    BlockedQueue.append(p)
                    if lock.locked():
                        lock.release()
                    if algo == 1:

                        LRUthread = Thread(target=LRU, args=(p.Pages[i], p,))
                        LRUthread.start()
                        LRUthread.join()

                    else:

                        FIFOthread = Thread(target=FIFO, args=(p.Pages[i], p,))
                        FIFOthread.start()
                        FIFOthread.join()

                    time = time + 1
                    Cycles += 5
                    out = True
                    break
                if algo == 1:
                    print("HIIIIIIIIIIIIIIII")
                    index = CheckInRam(p.Pages[i])
                    if index != -1:
                        Memory[index].RefBit = True
                        Cycles += MemoryCyclesHit
                        Memory.remove(Memory[0])
                        Memory.append(p.Pages[i])


            time = time + 1
            ProcessArrival()
            p.PageLocation = p.PageLocation + 1
            p.setRemaining(p.Remaining - 1)

            if p.Remaining == 0:
                p.ended = True
                if lock.locked():
                    lock.release()
                break
        if out:
            if lock.locked():
                lock.release()
            continue

        if p.Remaining == 0:
            p.end = time
            p.ended = True
            p.TurnAroundTime = p.end - p.begin
            p.Waiting = p.TurnAroundTime - p.duration
            if lock.locked():
                lock.release()

        p.join()
        temp = ReadyQueue.popleft()
        if not p.ended:
            ReadyQueue.append(temp)
            if lock.locked():
                lock.release()

        prev = p


def ProceessesFinished():
    for process in Processes:
        if process.end == -1:
            return False

    return True


def ProcessArrival():
    global time
    for process in Processes:
        if process.begin == time and ReadyQueue.count(process) == 0:
            ReadyQueue.append(process)



def CheckInRam(page):
    for index in range(0, len(Memory)):
        if Memory[index].Pid == page.Pid and Memory[index].Pnum == page.Pnum:
            return index

    return -1


def CheckMemory(page,process):
    global Cycles
    index = CheckInRam(page)
    if index != -1:
        Memory[index].BitReference = True
        Cycles += MemoryCyclesHit
        return True
    else:
        return False


def FIFO(page, process):
    global Cycles, MemoryIndex, MemorySize, NumOfPages, PageFaults,lock
    lock.acquire()
    if NumOfPages < len(Memory):
        Memory[MemoryIndex] = page
        Memory[MemoryIndex].RefBit = True
        FIFOList.put(MemoryIndex)
        NumOfPages += 1
        MemoryIndex = (MemoryIndex + 1) % MemorySize
    else:
        VictimIndex = FIFOList.get()
        FIFOList.put(VictimIndex)
        #page.RefBit = True
        Memory[VictimIndex] = page
        # Check REfBIT
    process.PageLocation = process.PageLocation + 1
    Cycles += DiskCyclesHit
    PageFaults += 1
    process.NumFaults += 1
    ReadyQueue.append(process)
    BlockedQueue.popleft()


def LRU(page, process):
    global Cycles, MemoryIndex, MemorySize, NumOfPages, PageFaults,lock,Memory
    lock.acquire()
    if NumOfPages >= len(Memory):
        Memory.remove(Memory[0])
        page.RefBit = True
        Memory.append(page)

    else:
        page.RefBit = True
        Memory[MemoryIndex] = page
        MemoryIndex = (MemoryIndex + 1) % MemorySize
        NumOfPages += 1

    process.PageLocation = process.PageLocation + 1
    Cycles += DiskCyclesHit
    PageFaults += 1
    process.NumFaults += 1
    ReadyQueue.append(process)
    BlockedQueue.popleft()




if __name__ == '__main__':
    menu()
    print("\nPage Faults =", end=" ")
    print(PageFaults)
    print("Cycles =", end=" ")
    Cycles += time * 1000
    print(Cycles)
    print("Time =", end=" ")
    print(time)
