# The BankOfChange!!

The bank with new concept.
We are the bank that gives the change for *free of cost*. Say, if you give me a 100 Rs. Note, then based on the available denominations, I should give you a change with denominations < 100 Rs. Notes.
It’s an automated system.

As a: Banker, in the morning, I should feed the machine with the different denominations & number of counts.
As an example: I may enter 
100 Rs. – 10 notes, 50 Rs. - 20 notes, 10 Rs. – 30 notes, 5 Rs. – 50 notes, 1 Rs. – 100 notes. I may choose to not feed the machine with 20 Rs. 
As a: Customer, I can enter only 1 note @ a time. 

Maximum possible denomination possible is 1000 Rs. bill. 

#### ATs:
1.	When I insert a note, it should give me the change starting from next highest note to lowest note.
2.	If exact change isn’t possible, it should return my note indicating the same.
3.	If the note I inserted is the lowest then it should indicate the same & inform that `we don't have denomination of lower value`
4.	If banker hasn’t deposited any denominations or say the machine has `no denominations` then  it should not allow user to insert any note, indicating that the machine is outoforder or empty.
5.	Banker should be notified if some of the notes which were orignially present go out of stock.
6.	In case of any failure, end user's note should be returned.

###### Future enhancements (** Currently, unclear when I would ask for it, may be after 6 months or may be after 2 years):-

1. It should give user an option to if we wants change with @ least 2 denominations. As an example: If I insert a 500 Rs. Bill, then I expect 100 X 4 + 50 X 2. 
2.	As a banker, I should better start earning some money @ some point in time. Monday through Friday change is for free. Saturday Sunday, user is charged @ 2% of charge (Ceiled to next Rs.). This charge needs to be deducted and only the remaining amounts change can be given.
3.	Banker should have ability to change the % charge or DOW pattern.

## Happy coding!! :smiley:
