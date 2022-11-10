from threading import Thread
import time
import threading
import sys
import socket
import traceback

if(len(sys.argv) != 2):
    print('oNode.py <bootstrapper>')
    sys.exit(2)

bootstrapper = sys.argv[1]
print("Bootstrapper:",bootstrapper)

DEFAULT_PORT = 8080
BUFFER_SIZE = 1024
HOST = "127.0.0.1"
vizinhos = []


def start_server():
   soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

   print("Socket created")
   try:
      soc.bind(("", DEFAULT_PORT))
   except:
      print("Bind failed. Error : " + str(sys.exc_info()))
      sys.exit()

   soc.listen(6) # queue up to 6 requests
   print("Socket now listening")

   # infinite loop- do not reset for every requests
   while True:
        connection, address = soc.accept()
        ip, port = str(address[0]), str(address[1])
        print("Connected with " + ip + ":" + port)

        try:
            Thread(target=clientThread, args=(connection, ip, port)).start()
        except:
            print("Thread did not start.")
            traceback.print_exc()

        soc.close()

def clientThread(connection, ip, port, max_buffer_size = 1024):
   is_active = True
   while is_active:
      client_input = receive_input(connection, max_buffer_size)
      if "--QUIT--" in client_input:
         print("Client is requesting to quit")
         connection.close()
         print("Connection " + ip + ":" + port + " closed")
         is_active = False
      else:
         print("Processed result: {}".format(client_input))
         connection.sendall("-".encode("utf8"))

def receive_input(connection, max_buffer_size):
   client_input = connection.recv(max_buffer_size)
   client_input_size = sys.getsizeof(client_input)

   if client_input_size > max_buffer_size:
      print("The input size is greater than expected {}".format(client_input_size))

   decoded_input = client_input.decode("utf8").rstrip()
   result = process_input(decoded_input)

   return result

def process_input(input_str):
   print("Processing the input received from client")
   return "Hello " + str(input_str).upper()

def sender():
   soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

   try:
      soc.connect((bootstrapper, DEFAULT_PORT))
   except:
      print("Connection Error")
      sys.exit()

   print("Please enter 'quit' to exit")
   message = input(" -> ")
   while message != 'quit':
      soc.sendall(message.encode("utf8"))
      if soc.recv(5120).decode("utf8") == "-":
         pass # null operation
      message = input(" -> ")
   soc.send(b'--quit--')
            
print("Initializing....")
            
#ip_receiver = input("\nEnter the IP of reciever: ")
#port_receiver = int(input("\nEnter the port of the reciever: "))

#ip_sender = input("\nEnter the IP of your system : ")
#port_sender = int(input("\nEnter the port of your system: "))

server_thread = Thread(target=start_server)
server_thread.start()

if(bootstrapper != "Server"):
    sender()