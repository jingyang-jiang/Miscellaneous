function p = s(val)
% x and y provided by question
x = linspace(-1,1,7);
y = arrayfun(@(n) 1/(1+25*n.^2),x);
n = length(x);
% find z 
h = zeros([1 n-1]);
b = zeros([1 n-1]);
for i = 1 : n-1
    h(i) = x(i+1)-x(i);
    b(i) = (y(i+1)-y(i))/h(i);
end 
%Forward elimination
u = zeros([1 length(h)-1]);
u(1) = 2*(h(1)+h(2));
v = zeros([1 length(h)-1]);
v(1) = 6*(b(2)-b(1));
for i = 2:length(h)-1
    mult = h(i)/u(i-1);
    u(i) = 2*(h(i)+h(i+1)) - mult * h(i);
    v(i) = 6*(b(i+1)-b(i)) - mult *v(i-1);
end
%Back substitution
z = zeros(size(x));
for i=length(h)-1:-1:1
    z(i+1) = (v(i)-h(i+1)*z(i+2))/u(i);
end
%interpolate

for i= 1:length(x)-1
    if (val - x(i+1)) <= 0 
        break
    end
end    
h = x(i+1) - x(i);
B = -h*z(i+1)/6 -h*z(i)/3 + (y(i+1)-y(i))/h;
D = (z(i+1)-z(i))/(6*h);
p = y(i) + (val - x(i))*(B+(val-x(i)))*(z(i)/2 + (val - x(i)*D));
end

