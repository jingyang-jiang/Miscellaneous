function x = detQ4(A)
% get det of A with LU factorization with partial pivoting
% input A: n by n matrix
% output x: determinant of A
[L,U,P]=lupp(A);

x = prod(diag(L))*prod(diag(U))*det(inv(P));

end

