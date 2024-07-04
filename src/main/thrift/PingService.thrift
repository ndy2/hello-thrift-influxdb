namespace java hello
#@namespace scala hello

include "Exceptions.thrift"

service PingService {

  string ping()

  string counter(
    1: string name
  )

  string throwMteDeclared() throws (
    1: Exceptions.MyThriftException e
  )
}
