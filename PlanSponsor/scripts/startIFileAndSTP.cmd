@echo off

start c:\apps\SubJournal\scripts\startSJ.cmd
sleep 2
start c:\apps\GFT\scripts\startGFT.cmd
sleep 2
start c:\apps\stp\scripts\startFileProcessor.cmd
sleep 2
start c:\apps\stp\scripts\startDataChecker.cmd
exit

