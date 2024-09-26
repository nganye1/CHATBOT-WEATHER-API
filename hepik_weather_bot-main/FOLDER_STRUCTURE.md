# Project Folder Structure

This document represents the agreed project's structure.
This structure is **NOT** to be violated but **RESPECTED** when making commits.

## Table of Contents

- [Team approval]()
- [Overview]()

## Team Approval

I, 

- [X] Harshiv Bharat Patel
- [X] Peter Nganye Yakura
- [x] Kelvin Kavisi,

have read and agreed to follow, respect the aforementioned folder structure and understand my peers will not approve any contribution if the above structure is violated.

## Overview

[Maven][maven_link] recommends this folder structure which adapts to project growth and is straightforward. The main directories will include:

- `src` - contains any source code that will be written.
- `docs` - contains documentation we have for this project
- `lib` - will contain libraries such `JUnit` etc. for all of us as we are using `IntelliJ`. This ensures predictable testing.
- `bin` - will contain outputs for any `JAR` files we regenerate when Java code changes.

### `src` sub-folders

All code will reside here. Its main subdirectories include:

1. `tests` - will contain any tests we write
2. `main` - will contain actual code
    - `java` - all `Java` code stays here
    - `gui` - code for any GUI (Graphical User Interface) built to extend chat bot logic
3. `docs` - will contain documentation
    - `research` - research information on our Chat bot
    - `chatbot` - documentation about the Chat bot. (Maybe create a wiki page from this?)

```text

HEPIK_weather_bot (root directory)
│
├── lib/
├── bin/
├── docs/
│    ├── research/
│    └── chatbot/
│
└── src/
     ├── tests/
     └── main/
          ├── java/
          └── gui/
          
```

[maven_link]: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html