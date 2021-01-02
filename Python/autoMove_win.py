# Script that automatically moves files:
#   - Sorts all files in Downloads folder into the specified directories
#   - (Not managing Pictures folder because Windows does not save screenshots
#      directly after using Snip&Sketch)
#
# To start the script on boot:
#   - follow: https://stackoverflow.com/questions/22383446/python-script-start-on-boot
#

import os
import time

from watchdog.observers import Observer 
from watchdog.events import FileSystemEventHandler

#######################
##  Downloads folder  #
#######################

baseDownloadsFolder = "C:/Users/Luka Kralj/Downloads"
downloadsFolder = os.path.abspath(baseDownloadsFolder)

# Extensions to sort
extFolders = {
    ".zip": "Archives",
    ".gz": "Archives",
    ".xz": "Archives",
    ".tar": "Archives",
    ".jpg": "Images",
    ".jpeg": "Images",
    ".png": "Images",
    ".gif": "Images",
    ".mp4": "Videos",
    ".webm": "Videos",
    ".pdf": "PDFs",
    ".pptx": "Documents",
    ".xlsx": "Documents",
    ".docx": "Documents",
    ".doc": "Documents",
    ".odt": "Documents",
    ".exe": "WinExecutables",
    ".msi": "WinExecutables"
}
ignoreExtensions = [".crdownload", ".ini"]

def on_modified():
    print("modified")
    for filename in os.listdir(downloadsFolder):
        fExt = os.path.splitext(filename)[1].lower()
        src = os.path.abspath(baseDownloadsFolder + "/" + filename)

        if os.path.isdir(src) or fExt in ignoreExtensions:
            continue

        now = time.mktime(time.localtime())
        fLastMod = time.mktime(time.localtime(os.path.getmtime(src)))

        if (now - fLastMod)/60 < 30: # Skip less than 30 min old files
            continue

        if fExt in extFolders.keys():
            dest = os.path.abspath(baseDownloadsFolder + "/" + extFolders[fExt])
        else:
            dest = os.path.abspath(baseDownloadsFolder + "/Other")

        try:
            os.mkdir(dest)
        except FileExistsError:
            pass 
        
        os.rename(src, os.path.abspath(dest + "/" + filename))


lastList = os.listdir(downloadsFolder)

def hasChanged():
    global lastList
    newList = os.listdir(downloadsFolder)
    toReturn = (lastList != newList)
    lastList = newList
    return toReturn

try:
    while True:
        time.sleep(10)
        if hasChanged():
            on_modified()

except KeyboardInterrupt:
    pass
