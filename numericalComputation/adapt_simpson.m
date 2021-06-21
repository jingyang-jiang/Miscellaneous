function numI = adapt_simpson(f,a,b,z,level,level_max)

h = b - a;
c = (a+b)/2;
I1= h*(f(a)+4*f(c)+f(b)) /6;
level = level + 1;
fprintf('level %d reached \n',level)
d = (a+c)/2;
e = (c+b)/2;
I2 = h*(f(a)+4*f(d)+2*f(c)+4*f(e)+f(b)) / 12;
if level >= level_max
    numI = I2;
    fprintf('max level %d reached\n',level_max)
else
    if abs(I2-I1) <= 15*z
        numI = I2+ 1/15*(I2-I1);
        
    else
        numI = adapt_simpson(f,a,c,z/2,level,level_max) + adapt_simpson(f,c,b,z/2,level,level_max);
        
    end
end 

end 