## WhatsApp Crypto Toolkit

Inspired by [mpg25](https://www.github.com/mgp25)'s Crypt12-Decryptor URL: https://github.com/mgp25/Crypt12-Decryptor
            

This project is supposed to be built with Maven:

```
mvn package
```

Once built, you can use it to crypt or decrypt WhatsApp files.


#### Decrypt
```
java -jar whatsapp-crypto-tk.jar decrypt -k key -i msgstore.db.crypt12 -o msgstore.db
```

#### Encrypt
```
java -jar whatsapp-crypto-tk.jar encrypt -k key -I msgstore.db -O msgstore.db.crypt12
```


### Known issues
The project works currently better with the OpenJDK. Oracle's Java refuses to start properly due to non-signed jars. 
 
 
 2018 Raphaël P. Barazzutti           