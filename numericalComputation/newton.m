function root=newton(fname,fdname,x,xtol,ftol,n_max,display,r)
% Newton’s method.
% input:
% fname string that names the function f(x).
% fdname string that names the derivative f’(x).
% x the initial point
% xtol and ftol termination tolerances
% nmax the maximum number of iteration
% display = 1 if step-by-step display is desired,
%         = 0 otherwise
% r the expected root.
% output: root is the computed root of f(x)=0
%
n = 0;
fx = feval(fname,x);
r = abs(r);
if display
    disp(' n      x                      f(x)                      relative error')
    disp('--------------------------------------------------------------------------')
    fprintf('%4d %23.15e %23.15e %23.15e\n', n, x, fx,abs(x-r)/r)
end
if abs(fx) <= xtol
    root = x;
    return
end
for n = 1:n_max
    fdx = feval(fdname,x);
    d = fx/fdx;
    x = x - d;
    fx = feval(fname,x);
    if display
    fprintf('%4d %23.15e %23.15e %23.15e\n',n,x,fx,abs(x-r)/r)
    end
    if abs(d) <= xtol || abs(fx) <= ftol
    root = x;
    return
    end
end






% bisection cannot guarantee complex roots becasue the complex parts of a,b
% may cancel each other other when we try to find mid point. If a,b are
% real to begin with, then they will converge to a real estimate.

