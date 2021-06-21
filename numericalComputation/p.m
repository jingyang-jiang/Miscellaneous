function y = p(val)

% x and y provided by question
x = linspace(-1,1,7);
y = arrayfun(@(n) 1/(1+25*n.^2),x);

% newton_coef and newton_pval provided in Lecture
co = newton_coef(x,y);
y = newton_pval(co,x,val);

end

