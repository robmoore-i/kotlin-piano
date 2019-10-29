# How to download the piano samples

The samples for this repository are from an American university: The University of Iowa.

They can be downloaded by using the script `download-piano-samples.py`. From the terminal, you will want to run the
command `python3 download-piano-samples.py`, which will go through the publically available piano samples and download
them.

The downloads take quite a while to complete. While they are downloading, occassionally a download will hang and not
complete. In this case, take the following steps:
- Take a mental note of the file that the script is attempting to download (you can read this from the printed logs in
the terminal)
- Stop the script. You can use Ctrl-C if you are running it from the terminal.
- Delete the partially downloaded file (the one I just said that you should remember) inside the `iowa/` directory.
- Rerun the script.

When the script is run, it will automatically read the contents of the `iowa` directory and make sure to skip the
download for any files which are already present.