function a = newton_coef(x,y)

np1 = length(x);
for k = 1 : (np1 - 1)
    y(k+1:np1)=(y(k+1:np1)-y(k)) ./ (x(k+1:np1)-x(k));
end 

a = y;

