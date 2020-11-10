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

downloadsFolder = r"C:\Users\Luka Kralj\Downloads"

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
    ".pdf": "PDFs",
    ".pptx": "Documents",
    ".xlsx": "Documents",
    ".docx": "Documents",
    ".odt": "Documents",
    ".exe": "WinExecutables"
}
ignoreExtensions = [".crdownload"]

class DownloadsHandler(FileSystemEventHandler):
    def on_modified(self, event):
        for filename in os.listdir(downloadsFolder):
            fExt = os.path.splitext(filename)[1].lower()
            src = downloadsFolder + "\\" + filename

            if os.path.isdir(src) or fExt in ignoreExtensions:
                continue

            now = time.mktime(time.localtime())
            fLastMod = time.mktime(time.localtime(os.path.getmtime(src)))

            if (now - fLastMod)/60 < 30: # Skip less than 30 min old files
                continue

            if fExt in extFolders.keys():
                dest = downloadsFolder + "\\" + extFolders[fExt]
            else:
                dest = downloadsFolder + "\\Other"

            try:
                os.mkdir(dest)
            except FileExistsError:
                pass 
            
            os.rename(src, dest + "\\" + filename)

downloads_handler = DownloadsHandler()
downloadsObserver = Observer()
downloadsObserver.schedule(downloads_handler, downloadsFolder, recursive=False)
downloadsObserver.start()


try:
    while True:
        time.sleep(10)
except KeyboardInterrupt:
    downloadsObserver.stop()

downloadsObserver.join()
