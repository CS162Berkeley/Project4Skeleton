Phase 4: Build a Distributed Key-Value Store
============================================

In Phase 4 of your class, you will implement a distributed Key-Value
Store that runs on multiple nodes on *Amazon EC2* and uses Two-Phase
Commit for atomic operations, replication for performance and
fault-tolerance, and encryption for security. As in previous phases, you
will submit an initial design document and a final design document apart
from the code itself.

![Architecture Diagram](http://inst.eecs.berkeley.edu/~cs162/sp12/pics/proj4-overview.png)

**Figure:** A distributed Key-Value Store with replication factor of
two.

![Architecture Diagram](http://inst.eecs.berkeley.edu/~cs162/sp12/pics/proj4-arch3.png)

**Figure:** Functional digram of a distributed key-value store.
Components in colors other than black are the key ones you will be
developing for this project. Blue depicts GET execution path, while
green depicts PUT and DEL; purple ones are shard between operations.
Components in black might also require minor modifications to suite your
purposes.

### Skeleton Code:

The project skeleton you **should** build on top of is posted at
[https://github.com/CS162Berkeley/Project4Skeletion](https://github.com/CS162Berkeley/Project4Skeletion).
If you have git installed on your system, you can run
`git clone https://github.com/CS162Berkeley/Project4Skeletion.git`, or
else you can download the tarball from
[here](https://github.com/CS162Berkeley/Project4Skeletion/zipball/master).
Phase 4 builds on top of the Single Server Key-Value Store developed in
Phase 3; however, several interfaces have been extended to support the
required functionalities for Phase 4. You can reuse the code developed
for Phase 3 and define additional classes and methods as you deem fit.

### EC2 instructions:

Read the instructions carefully from
[this](http://inst.eecs.berkeley.edu/~cs162/sp12/ec2-spec.pdf) document
regarding instantiation, operation, and termination of EC2 instances. As
always, let us know in Piazza if anything is unclear.

### Requirements:

-   **Unless otherwise specified below, you will have to satisfy the
    requirements described in [Phase
    3](http://inst.eecs.berkeley.edu/~cs162/sp12/phase3.html).**
-   Each Key will be stored using 2PC to two Key-Value servers, which
    will be selected using consistent hashing. There will be **at least
    two** Key-Value servers in the system. See below for further
    details.
-   Key-value servers will have 128-bit globally unique IDs (use
    `java.util.UUID`), and they will register with the coordinator with
    that ID when they start. For simplicity, you can assume that the
    total number of Key-Value servers are fixed, and they always come
    back with the same ID if they crash. Note that this simplification
    will cause the system to block on any failed Key-Value server.
    However, not assuming this will require dynamic successor adjustment
    and re-replication, among other changes.
-   You do not have to support concurrent update operations irrespective
    of which Key they are working on (i.e., 2PC PUT and DELETE
    operations are performed one after another), but retrieval
    operations (i.e., GET) of different Keys must be concurrent unless
    restricted by an ongoing update operation on the same Key.
-   Coordinator communication to individual replicas must be performed
    in parallel, i.e., requests for vote and dissemination of global
    decisions must be performed using multiple threads.
-   For this particular project, you can assume that the Coordinator
    will not crash. Consequently, there is no need to log its states,
    nor does it require to survive failures. Individual Key-Value
    servers, however, must log necessary information to survive from
    failures.
-   The coordinator server will include a *write-through* LRU cache,
    which will have the same semantics as the write-through cache you
    used before. Caches at Key-Value servers will still remain
    write-through.
-   You should bulletproof your code, such that the no server crashes
    under any circumstances.
-   For your final submission, you will submit your code and host your
    applications (part 1 and part 5) on an EC2 instance. You will submit
    the access details for the instance along with your code.
-   You will run the client interface of the Coordinator service on port
    8080.
-   Individual Key-Value servers must use random ports assigned upon
    creating respective SocketServers for listening to 2PC requests and
    register themselves with the 2PC interface of the Coordinator
    service running on port 9090.

### Tasks (weight / approximate lines of code):

1.  *(40% / 250 loc)* Implement the *2PCMaster* class that implements
    2PC coordination logic in the Coordinator server. 2PCMaster must
    select replica locations using consistent hashing.
2.  *(35% / 220 loc)* Implement the *2PCMasterHandler* class that
    implements logic for 2PC participants in Key-Value servers.
3.  *(10% / 75 loc)* Implement registration logic in SlaveServer (aka
    Key-Value Server) and RegistrationHandler in 2PCMaster. Each
    SlaveServer has a unique ID that it uses to register with the
    2PCMaster in the coordinator.
4.  *(10% / 75 loc)* Implement the *2PCLog* class that Key-Value servers
    will use to log their states during 2PC operations and for
    rebuilding during recovery.
5.  *(5% / 40 loc)* Implement encryption/decryption of stored values.
    The first message to be exchanged between client and server has to
    be an encryption key. Add a new message type "getEnKey" to the
    existing message types. The server has a single encryption key
    generated when it starts and shares this key with clients when
    requested using "getEnKey".You can store the values in encrypted
    format at server end.

### Consistent Hashing

As mentioned earlier, Key-Value servers will have unique 128-bit IDs.
The coordinator will hash the Keys to 128-bit address space. Then each
Key-Value server will be responsible for storing Keys with hash values
greater than the ID of its immediate predecessor up to its own ID.

![Consistent Hashing](http://inst.eecs.berkeley.edu/~cs162/sp12/pics/consistent-hashing.png)

**Figure:** Consistent Hashing. Four Key-Value servers and three hashed
Keys along with where they are placed in the 128-bit address space.

### Sequence of Operations During Concurrent GET and PUT (DELETE) Requests

![Sequence Diagram of Concurrent R/W Operations in Phase
4](http://inst.eecs.berkeley.edu/~cs162/sp12/pics/seq-diag-phase4.png)

**Figure:** Sequence diagram of concurrent Read/Write operations in
Phase 4. "Phase 3" blocks in the digram refer to the sequence diagram of
activities when clients write to a single-node Key-Value server, where
the Coordinator is the client to individual Key-Value servers. GET
request from Client 1 is hitting the cache in the above diagram; if it
had not, the GET request would have been forwarded to each of the
SlaveServers until it is found.

![Sequence Diagram of Concurrent R/W Operations in Phase
3](http://inst.eecs.berkeley.edu/~cs162/sp12/pics/seq-diag-phase3.png)

**Figure:** Sequence diagram of concurrent Read/Write operations in
Phase 3.

### Your Key-Value server should support the GET/PUT/DELETE interface using the same format as Phase 3 with the following changes:

-   **Responses to Clients for 2PC operations:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="resp"\>\
     <Message\>Committed/Aborted</Message\>\
     </KVMessage\>

### Your Coordinator Server should perform Two-Phase Commit using the following extended KVMessage format (2PCMessage piggybacked on KVMessage):

-   **2PC Put Value Request:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="putreq"\>\
     <Key\>key</Key\>\
     <Value\>value</Value\>\
     <TPCOpId\>2PC Operation ID</TPCOpId\>\
     </KVMessage\>\
-   **2PC Delete Value Request:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="delreq"\>\
     <Key\>key</Key\>\
     <TPCOpId\>2PC Operation ID</TPCOpId\>\
     </KVMessage\>\
-   **2PC Responses:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="ready/abort"\>\
     <TPCOpId\>2PC Operation ID</TPCOpId\>\
     </KVMessage\>
-   **2PC Decisions:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="commit/abort"\>\
     <TPCOpId\>2PC Operation ID</TPCOpId\>\
     </KVMessage\>
-   **2PC Acknowledgement:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="ack"\>\
     <TPCOpId\>2PC Operation ID</TPCOpId\>\
     </KVMessage\>

### Registration Messages

-   **Register:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="register"\>\
     <Message\>SlaveServerID@HostName:Port</Message\>\
     </KVMessage\>
-   **Registration ACK:**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="resp"\>\
     <Message\>Successfully registered
    SlaveServerID@HostName:Port</Message\>\
     </KVMessage\>

### Encryption Messages

-   **Encryption Key request**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="getEnKey"\>\
     </KVMessage\>
-   **Server response**\
     <?xml version="1.0" encoding="UTF-8"?\>\
     <KVMessage type="resp"\>\
     <Message\>Encryption Key</Message\>\
     </KVMessage\>

### Error messages in addition to the ones in Phase 3:

-   "Registration Error: Received unparseable slave information" --
    Registration information was not in "slaveID@hostName:port" format.

### Concepts you are expected to learn

-   Two-Phase Commit
-   Encryption
-   Logging and Recovery using Logs
-   Consistent Hashing
-   Failure Detection using Timeouts

