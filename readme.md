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

  `mvn package`

 `docker build -t alysenkofx/zip_web:zip .`

or

`docker pull alysenkofx/zip_web:zip`

and then run

 `docker run -p 8080:8080 alysenkofx/zip_web:zip`
 - Work in progress...