#Products service

This service was created for interview purpose.
<br />
It fullfils following requirements:  

* Create a new product
* Retrieve a list of all products
* Update a product
* Delete a product (​ soft deletion​ )
<br />
<br />
Each product should have a SKU (unique id), a name, a price and date it was created.

<br />

You can build and run it with standard spring boot command
<br />

**mvn spring-boot:run**

<br />

Of if you wish to package it then use 

<br />

**mvn clean package**

<br />

and put output jar in environment which you prefer and run with command

<br />

**mvn -jar <jar-file-name>.jar**

<br />

##API 

API consists of four endpoints consistent with REST approach:
<br />
__POST /products__ - create new product with name and price (JSON in request body)  
<br />
__GET /products__ - get all active products (not obsolete - means softly deleted)
<br />
__PUT /products__ - updates product with changes (JSON in request body)  
<br />
__DELETE /products/{sku}__ - softly deletes product (changes obsolete flag to true)

 