from django.urls import path
from . import views 

urlpatterns = [

    path("",views.index,name = "index"),
    path("jingyang",views.jingyang,name= "jingyang"),
    path("<str:name>",views.greet,name = "greet"),
]