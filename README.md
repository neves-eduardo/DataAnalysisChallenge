# DataAnalysisChallenge
## Overview
![alt text](https://i.imgur.com/jB5UAy1.jpg?1)
### How to run this project:
1) Use the commands:
> .gradlew clean build
> .gradlew run

2) Create a folder named "data" in your home directory, and inside it two more folders, one named "in" and the other "out"

3) create a file named example.dat with the following text and move it to the "in" folder:

"001ç1234567891234çDiegoç50000  
  001ç3245678865434çRenatoç40000.99  
  002ç2345675434544345çJose da SilvaçRural    
  002ç2345675433444345çEduardoPereiraçRural  
  003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego  
  003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato"
  
4) A file named example.done.dat should appear in the "out" folder now if the app is running, check it out!

### Future Improvements
- Support for new formats
- Better error handling
- Read Files on update, not only on creation
