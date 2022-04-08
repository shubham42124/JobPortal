<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form>
  <div class="row mb-3">
    <label for="inputcompanyname" class="col-sm-2 col-form-label">Company Name</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputcompanyname" placeholder="Enter Company Name">
    </div>
  </div>
  <div class="row mb-3">
    <label for="inputHeadquarters" class="col-sm-2 col-form-label">Headquarter</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputHeadquarters" placeholder="Enter Headquarters">
    </div>
  </div>
 <div class="row mb-3">
    <label for="inputcompanyuser" class="col-sm-2 col-form-label">Company Username</label>
    <div class="col-sm-10">
      <input type="email" class="form-control" id="inputcompanyuser" placeholder="Company Username">
    </div>
  </div>
 <div class="row mb-3">
    <label for="inputcompanypass" class="col-sm-2 col-form-label">Password</label>
    <div class="col-sm-10">
      <input type="password" class="form-control" id="inputcompanypass" placeholder="Password">
    </div>
  </div>

<div class="row mb-3">
    <label for="inputdescription" class="col-sm-2 col-form-label">Description</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="inputdescription" placeholder="Description">
    </div>
  </div>


  <fieldset class="row mb-3">
    <legend class="col-form-label col-sm-2 pt-0">Verified</legend>
    <div class="col-sm-10">
      <div class="form-check">
        <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios1" value="option1" checked>
        <label class="form-check-label" for="gridRadios1">
          True
        </label>
      </div>
      <div class="form-check">
        <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios2" value="option2">
        <label class="form-check-label" for="gridRadios2">
          False
        </label>
      </div>
     
     
    </div>
  </fieldset>
  <div class="row-mb-3">
    <label for="inputcode" class="col-sm-2 col-form-label">State</label>
<div class ="col-sm-5"
     <input type="text" class="form-control" id="inputcode">
  </div>
</div>
  <button type="submit" class="btn btn-primary">Sign in</button>
</form>

</body>
</html>