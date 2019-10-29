import os

import requests

volumes = ["pp", "mf", "ff"]
octaves = [1, 2, 3, 4, 5, 6, 7]
notes = "Bb B C Db D Eb E F Gb G Ab A".split(" ")
edges = [("pp", 0, "Bb"), ("pp", 0, "B"), ("pp", 8, "C"), ("mf", 0, "B"), ("mf", 8, "C"), ("ff", 0, "A"),
         ("ff", 0, "Bb"), ("ff", 0, "B"), ("ff", 8, "C")]


# See: http://theremin.music.uiowa.edu/MISpiano.html
def download_url(volume, octave, note):
    filename = "Piano." + volume + "." + note + str(octave) + ".aiff"
    return "http://theremin.music.uiowa.edu/sound%20files/MIS/Piano_Other/piano/" + filename


def all_piano_sample_urls():
    urls = {}
    for volume in volumes:
        for octave in octaves:
            for note in notes:
                urls[volume + str(octave) + note] = download_url(volume, octave, note)

    for edge in edges:
        urls[edge[0] + str(edge[1]) + edge[2]] = download_url(edge[0], edge[1], edge[2])

    return urls


def download_piano_sample(url, target_file):
    if os.path.isfile(target_file):
        print("Skipping target file '" + target_file + "' because it already exists")
        return False
    with open(target_file, "wb") as f:
        response = requests.get(url)
        if response.status_code != 200:
            print("Failed to get piano sample at url '" + url + "' - status code was " + str(response.status_code))
            exit(1)

        mb = 100000
        for chunk in response.iter_content(mb):
            f.write(chunk)

    return True


def download_all_piano_samples(target_folder):
    urls = all_piano_sample_urls()
    for note_specification in urls:
        print("Downloading piano sample for note specification '" + note_specification + "'")
        url = urls[note_specification]
        download_piano_sample(url, target_folder + "/" + note_specification + ".aiff")

    return True


# To download all the samples, on Rob's Zuhlke Mac, required around 60 minutes.
if __name__ == "__main__":
    download_all_piano_samples("../src/main/resources/iowa")
