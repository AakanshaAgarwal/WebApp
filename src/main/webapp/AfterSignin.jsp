<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head>
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 100%;
}
td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}
tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
</head>
<body>

<h2>Url Table</h2>

<table>
  <tr>
    <th>Long url</th>
    <th>Short url</th>
  </tr>
<c:forEach var="item" items="${list}">
   <tr>
    <td>${item.longUrl}</td>
    <td>${item.shortUrl}</td>
  </tr>
  </c:forEach>
  
</table>

</body>
</html>