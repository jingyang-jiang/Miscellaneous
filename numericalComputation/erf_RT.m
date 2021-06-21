%compute erf(2) with recursive trapezoid rule
tolerance = 10^-5;
f = @(x)(exp(-x^2));
coef = 2/ sqrt(pi);
n = 1;
a = 0;
b = 2;
h = (b-a)/(2^n);
% first iteration
I1 = 1/2 * h * (f(a)+f(b)) + h * f(a+h);
fprintf('n:%d result:%e error:%e \n',n,coef*I1,abs(coef*I1 - erf(2)))
newline
while abs(coef*I1 - erf(2)) > tolerance
    n = n + 1; 
    h = h/2;
    sum = 0;
    for i = 1 : 2^(n-1)
        sum = sum + f(a+(2*i-1)*h);
    end 
    I1  = 1/2*I1 + h*sum;
    fprintf('n:%d result:%e error:%e \n',n,coef*I1,abs(coef*I1 - erf(2)))
end


