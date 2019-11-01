# Custom Security Service

this project is created to give a try to custom microservices

Custom security service with token auth made up with 3 different apps 
there's 3 branches to look at:
<p><b>master</b> is a branch for auth service itself, it's main goal is to register and login users (also adding tokens to them)</p>
<p><b>api_gateway</b> is an orphan branch for resolving directions (eg. if there's no auth token or it's expired --> redirect 
to auth application, else --> redirects to the main ap) </p>
<p><b>blog_app</b> is an orphan branch and itself is the main app with restfull endpoints</p>
<p><b><i>NOTE:</i></b> apps must be published on the specified ports </br>eg. apiGateway must be located on 80 port </br>blog must be on 8080 </br>auth service must be on 9090 </p>  

