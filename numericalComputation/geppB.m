function X = geppB(A,B)
% gepp with size of B being n*p
% input: A is an n x n nonsingular matrix
% B is an n x p vector
% output: X is the solution of AX=B.
[n,p]=size(B);

for k = 1:n-1  
   [maxval, maxindex] = max(abs(A(k:n,k)));
   q = maxindex+k-1;
   if maxval == 0, error('A is singular'), end
   if q ~= k  
       A([k,q],k:n) = A([q,k],k:n);
       B([k,q],:) = B([q,k],:);
   end
   i = k+1:n;
   A(i,k) = A(i,k)/A(k,k);  
   A(i,i) = A(i,i) - A(i,k)*A(k,i); 
   B(i,:) = B(i,:) - A(i,k)*B(k,:);    
end

X = zeros(n,p);
for k = n:-1:1  
  X(k,:) = (B(k,:) - transpose(transpose(X(k+1:n,:))*transpose(A(k,k+1:n))))/A(k,k);
end

