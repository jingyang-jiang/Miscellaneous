function root=secant(fname,x0,x1,xtol,ftol,n_max,display,r)
% Newton’s method.
% input:
% fname string that names the function f(x).
% fdname string that names the derivative f’(x).
% x the initial point
% xtol and ftol termination tolerances
% nmax the maximum number of iteration
% display = 1 if step-by-step display is desired,
%         = 0 otherwise
% r the expected root
% output: root is the computed root of f(x)=0
%
r= abs(r);
fx0 = feval(fname,x0);
fx1 = feval(fname,x1);
if display
    disp(' n      x                      f(x)                      relative error')
    disp('--------------------------------------------------------------------------')
end
for n = 1:n_max
    
    d = (x1-x0)/(fx1-fx0)*fx1;
    x0 = x1;
    fx0 = fx1;
    x1 =  x1 - d;
    fx1 = feval(fname,x1);
    if display
    fprintf('%4d %23.15e %23.15e %23.15e\n', n, x1, fx1 ,abs(x1-r)/r)
    end
    if abs(d) <= xtol || abs(fx1) <= ftol
    root = x1;
    return
    end
end