Important point to do before this project was turned into a production application - i.e. what would you improve/add, given more time.

1. More validation for Request attribute.
2. Implementation of Interceptor for verification of white-list character in the request body to avoid any wrong request to reach to controller/resource.
3. Implementation of Transactional Manager over debit and credit process.
4. More logging for prod application debugging and move all hard-coded error messages from java files to properties file.
5. Response object can carry more detail info about transactions
6. More meaning full and different http response for different scenario.
7.Implementation of Global Exception handler which will be used whenever a exception propagates uncaught throughout the system.
6. Security implementation for authentication of request (like JWT ).