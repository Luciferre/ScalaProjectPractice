# ScalaProjectPractice
Please look at the Local.scala, Remote.scala, Remote1.scala, Remote2.scala.
Please run as this order: Remote.scala, Remote1.scala, Remote2.scala, Local.scala.
Because that not all the nodes are running at the same time. Local will say hello to Remote, Remote1, Remote2. Remote will say hi to Remote1 and Remote2. Remote1 will say hi to Remote2. As a result the status of nodes will change according to the numbers of act and hi they have received.
