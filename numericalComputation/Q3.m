A = pascal(10);
disp('index     Relative Error      eps*Conditional     Relative Residual')
for i = 1:10
    X = randn(10,5);
    B = A*X;
    XC = geppB(A,B);
    
    E = norm(XC - X,'fro')/norm(X,'fro');
    CON = eps*cond(A,'fro');
    residual = norm(B-A*XC,'fro')/(norm(A,'fro')*norm(XC,'fro'));
    fprintf('%d        %e        %e        %e     \n',i,E,CON,residual);
end 
disp('i) relative error is mostly bounded by or close to eps*Conditional')
disp('ii)relative residual is bounded by eps')
