function pval = newton_pval(a,x,xx)

np1 = length(a);
pval = a(np1) * ones(size(xx));

for i = (np1-1):-1:1
    pval = a(i) + (xx - x(i)) .* pval;
end

