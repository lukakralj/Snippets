# Script that automatically moves files:
#   - Sorts all files in Downloads folder into the specified directories
#   - Stores old screenshots into a subfolder in the Pictures folder
#
# To start the script on boot edit /etc/crontab. Add line:
#
#   @reboot lukakralj python3 /home/lukakralj/Projects/Snippets/Python/autoMove.py
#

import os
import time

from watchdog.observers import Observer 
from watchdog.events import FileSystemEventHandler

#######################
##  Downloads folder  #
#######################

downloadsFolder = "/home/lukakralj/Downloads"

# Extensions to sort
extFolders = {
    ".zip": "Archives",
    ".gz": "Archives",
    ".tar": "Archives",
    ".jpg": "Images",
    ".jpeg": "Images",
    ".png": "Images",
    ".gif": "Images",
    ".mp4": "Videos",
    ".pdf": "PDFs",
    ".pptx": "Documents",
    ".xlsx": "Documents",
    ".docx": "Documents",
    ".odt": "Documents",
}
ignoreExtensions = ["crdownload"]

class DownloadsHandler(FileSystemEventHandler):
    def on_modified(self, event):
        for filename in os.listdir(downloadsFolder):
            fExt = os.path.splitext(filename)[1].lower()
            src = downloadsFolder + "/" + filename
            
            if os.path.isdir(src) or fExt in ignoreExtensions:
                continue

            if fExt in extFolders.keys():
                dest = downloadsFolder + "/" + extFolders[fExt]
            else:
                dest = downloadsFolder + "/Other"

            try:
                os.mkdir(dest)
            except FileExistsError:
                pass 

            os.rename(src, dest + "/" + filename)

downloads_handler = DownloadsHandler()
downloadsObserver = Observer()
downloadsObserver.schedule(downloads_handler, downloadsFolder, recursive=False)
downloadsObserver.start()

######################
##  Pictures folder  #
######################

picturesFolder = "/home/lukakralj/Pictures"

class PicturesHandler(FileSystemEventHandler):
    def on_modified(self, event):
        for filename in os.listdir(picturesFolder):
            src = picturesFolder + "/" + filename
            
            if os.path.isdir(src) or not filename.startswith("Screenshot"):
                continue

            now = time.mktime(time.localtime())
            fLastMod = time.mktime(time.localtime(os.path.getmtime(src)))

            if (now - fLastMod)/3600/24 < 1: # Skip less than a day old Screenshots
                continue

            dest = picturesFolder + "/OldScreenshots"
            try:
                os.mkdir(dest)
            except FileExistsError:
                pass 

            os.rename(src, dest + "/" + filename)

pictures_handler = PicturesHandler()
picturesObserver = Observer()
picturesObserver.schedule(pictures_handler, picturesFolder, recursive=False)
picturesObserver.start()


os.system("notify-send -u low -t 5000 \"AutoMove.py has started\"")
try:
    while True:
        time.sleep(10)
except KeyboardInterrupt:
    downloadsObserver.stop()
    picturesObserver.stop()

downloadsObserver.join()
picturesObserver.join()