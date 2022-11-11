
#confirmar se esta certo
import socket
from threading import Thread


def readConfigFile(myID):
    fp = open("config.txt", "r")
    config = fp.read()
    fp.close()
    config = config.split("\n")
    lista = []
    for line in config:
        if line == "":
            continue
        line = line.split(" ")
        lista.append(line)
    return lista



