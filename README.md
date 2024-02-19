# ST5008CEM-Programming-for-Developers
Assignment Submission
Question 1 - 6 (Package: assignment-final)
https://github.com/ashishmool/ST5008CEM-Programming-for-Developers/tree/1a6d5bd27fcb562511b9871e4968cf6061751248/assignment-final


Question No. 7 (Package: Question7-FriendApp-220417-main)
https://github.com/ashishmool/ST5008CEM-Programming-for-Developers/tree/1a6d5bd27fcb562511b9871e4968cf6061751248/Question7-FriendApp-220417-main


Logic for Recommendation System:
1.	Retrieve User Data: Fetch the necessary data from the database tables. Information about users and their friends.
2.	Build Graph: Construct a graph where each user is a node, and there is an edge between two users if they are friends. Can use adjacency list or adjacency matrix representation.
3.	Find Mutual Friends: For each pair of users who are not friends, find their mutual friends by traversing the graph.
4.	Generate Recommendations: Based on the mutual friends found, suggest friends to users who are not already connected.
