# Coconut-IDE
[![Build Status](https://travis-ci.org/MrChebik/Coconut-IDE.svg?branch=master)](https://travis-ci.org/MrChebik/Coconut-IDE)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c4acd2aa137745849973890abad2f67a)](https://www.codacy.com/app/mrchebik/Coconut-IDE?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MrChebik/Coconut-IDE&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/b5e31acb-b0be-41c1-91cf-7d8d3c88fc84)](https://codebeat.co/projects/github-com-mrchebik-coconut-ide-master)
> IDE for Java developers.

## Features
* Autocomplete (basic keywords)
* Highlight (without names) / Syntax / Paired symbols
* IO (Input/Output streams)
* Launch multi _java_ files from source folder (NOT libraries)
* Setting JDK path
* TreeStructure of project

## Demonstration
![Coconut-IDE - Screenshot](https://github.com/MrChebik/Coconut-IDE/blob/master/coconut-ide-demonstration.webp?raw=true)

Hello World - Error - Autocomplete

## Build
```
$ git clone https://github.com/MrChebik/Coconut-IDE.git
$ cd Coconut-IDE
$ mvn package
$ java -jar ./target/coconut-ide*.jar
```

## License
This project is licensed under the GPL-3.0 License - see the [LICENSE](https://github.com/MrChebik/Coconut-IDE/blob/master/LICENSE) file for details.