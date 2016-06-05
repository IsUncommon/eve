## Eve store commands.

#### Set Java primitive data types.

```java
  
  set(String key, int data)
  set(String key, int[] data)
  set(String key, float data)
  set(String key, float[] data)
  set(String key, double data)
  set(String key, double[] data)
  set(String key, long data)
  set(String key, long[] data)
  set(String key, byte data)
  set(String key, byte[] data)
  set(String key, char data)
  set(String key, char[] data)

```

#### Set Java Strings.

```java

  set(String key, String data)
  set(String key, String[] data)

```

#### Set Java List.

```java

  set(String key, List<?> data)
  
```

#### Set Java generic types.

Generic types look to converter to serialize and deserialize data.

```java

  set(String key, Object data)

```

#### Get all types.

Get is based on the return type of the variable.

```java

  T get(String key)

```

#### Query store based on KEY prefix and CLASS type.

Get list of ENTRIES for key prefix and Class type.

```java 

  query()
    .keyPrefix(String prefix)
    .ofType(Class cls)
    .entries();

```

Get list of KEYS for key prefix and class type.

```java 

  query()
    .keyPrefix(String prefix)
    .ofType(Class cls)
    .keys();

```

Get list of VALUES for key prefix and class type.

```java 

  query()
    .keyPrefix(String prefix)
    .ofType(Class cls)
    .values();

```

#### Get list of ENTRIES for key prefix and ANY class type.

```java 

  query()
    .keyPrefix(String prefix)
    .anyType()
    .entries();

```

#### Query store based on KEY contains and CLASS type.

Get list of ENTRIES for key contains and Class type.

```java 

  query()
    .keyContains(String contains)
    .ofType(Class cls)
    .entries();

```

Get list of KEYS for key contains and class type.

```java 

  query()
    .keyContains(String contains)
    .ofType(Class cls)
    .keys();

```

Get list of VALUES for key contains and class type.

```java 

  query()
    .keyContains(String contains)
    .ofType(Class cls)
    .values();

```

#### Get list of ENTRIES for key contains and ANY class type.

```java 

  query()
    .keyContains(String contains)
    .anyType()
    .entries();

```

#### Get list of KEYS for type.

```java

  query()
    .type(Class type)
    .keys();

```
