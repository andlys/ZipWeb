## Web service to archive and store files in cloud, based on:
https://github.com/andlys/Java-Zip

## Features:
- Upload any file or files to the server
- Download **single file** from the server in zip archive
- Download **all the files at once** from the server in zip archive
- Supports Windows and Unix server file systems 
## Usage

 - Start `ZipWebApplication` class

 - Open a page and upload a file

   - http://localhost:8080/uploadForm
 - Run in docker:

  `cd <project_folder>`

 `docker build -t alysenko/zip_web .`

 `docker run -p 8080:8080 alysenko/zip_web`
 - Work in progress...