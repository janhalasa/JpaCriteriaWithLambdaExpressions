# JpaCriteriaWithLambdaExpressions #

**This project is deprecated** in favour of [jpa-repositories-with-lambda-expressions](https://github.com/janhalasa/jpa-repositories-with-lambda-expressions), 
which is a library published on the Maven Central repository. 

Simple Java project demonstrating how to use Lambda expressions with JPA Criteria API.

The project shows how to write Java Persistence API (JPA) Criteria queries in an easier way using Lambda expressions added to Java in version 8. You can take its code and use it in your own projects.

### Background ###

I was thinking about how to make Criteria API code easier. The problem is that you usually need several objects to build a query and the calls cannot be streamlined. it’s possible to write a callback interface with a method getting all the necessary parameters to build a query, but that’s just making a new API on top of the existing one without it being significantly easier. Then I realized that Java 8 comes with Lambda expression support. It allows us to write basic functions without a need to wrap them in classes. Lambda expressions can be used as method parameters where an object implementing a single method interface is expected.

## Usage ##

Just download the project source files, and build it with Maven

`mvn package`

The project contains a main class, so it's executable. The main class creates an in-memory H2 database, inserts some records and then calls queries written as Lambda expressions.

`java -jar target/JpaCriteriaWithLambdaExpressions-1.0-SNAPSHOT.jar`

 The most interesting class of the project is the [AbstractDao class](https://github.com/janhalasa/JpaCriteriaWithLambdaExpressions/blob/master/src/main/java/com/halasa/criterialambda/dao/AbstractDao.java) and its usage in the [CarDao class](https://github.com/janhalasa/JpaCriteriaWithLambdaExpressions/blob/master/src/main/java/com/halasa/criterialambda/dao/CarDao.java).
 
## Preview ##
 
 These are  some examples of using Lambda expressions for building criteria queries using a DAO support method:
 
 ```java
 findWhere(Car.class, (cb, root, query)
    -> (query.where(cb.equal(root.get(Car_.colour), colour))
             .orderBy(cb.desc(root.get(Car_.id)))));
```

```java
createQuery(Car.class, (cb, root) -> (cb.equal(root.get(Car_.colour), colour)))
        .setMaxResults(10)
        .setFirstResult(5)
        .getResultList();
```
