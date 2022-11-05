import time
import threading
import sys
import socket

if(len(sys.argv) != 3):
    print('oNode.py <bootstrapper> <config_file>')
    sys.exit(2)

bootstrapper = sys.argv[1]
config_file = sys.argv[2]

print("Bootstrapper:",bootstrapper)
print("Config File:",config_file)

DEFAULT_PORT = 8080
BUFFER_SIZE = 20
vizinhos = []

def receiver():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(bootstrapper, DEFAULT_PORT)
    s.listen(1)

    conn, addr = s.accept()
    print('Connection address:', addr)
    while 1:
        data = conn.recv(BUFFER_SIZE)
        if not data: break
        print("received data:", data)
        conn.close()

def sender():
    #Create socket
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(bootstrapper,DEFAULT_PORT)
    #Send message
    text = "hello"
    while True:
        if "bye" in text or "exit" in text or "finish" in text:
            s.close()
            exit()
        else:
            text = input(f'{name}:')
            text = name+":"+text
            s.send(text.encode())
            
print("Initializing....")
            
#ip_receiver = input("\nEnter the IP of reciever: ")
#port_receiver = int(input("\nEnter the port of the reciever: "))

#ip_sender = input("\nEnter the IP of your system : ")
#port_sender = int(input("\nEnter the port of your system: "))

name = input("Enter your name: ")

#print("Waiting for client....")
#time.sleep(1)
#print("Connection established....")

# Using Multi-threading
send = threading.Thread(target=sender)
receive = threading.Thread(target=receiver)

send.start()
receive.start()