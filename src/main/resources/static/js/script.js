 console.log("this is script file");

var i=1;
b=document.querySelector('.sidebar');
a=document.querySelector('.content');

 const toggleSidebar= () =>{

   if(i==1){
     
     b.style.setProperty("display", "none");
     a.style.setProperty("margin-left", "5%");
     
    i=0;
   }
   else{
   
   
    b.style.setProperty("display", "block");
     a.style.setProperty("margin-left", "20%");
     i=1;
}

};

// const search=()=>{

//   let query= $("#search-input").val();
  

//   if(query=="")
//   {
//     $(".search-result").hide();

//   }
//   else{
   
//     //sending request to server

//     let url=`http://localhost:8090/search/${query}`;

//     fetch(url).then((Response) => {

//       return Response.json();
//     })

//     .then((data)=>{

//       console.log(data);

//     })
     
//     console.log(query);
//     $(".search-result").show();

//   }

// }