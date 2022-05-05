A = ones(7) - diag(ones(1,6), 1) - diag(ones(1,5), 2) - diag(ones(1,2), -5) - diag(ones(1,1), -6);
A = -A;

b = -[8; 6; 5; 4; 6; 7; 9];

c = ones(7,1);

intcon = [1,2,3,4,5,6,7];
options = optimoptions('intlinprog', 'Display', 'off');
% I
[x, fval, exitflag, output] = intlinprog(c, intcon, A, b, [],[],[1,1,1,1,1,1,1], [], options);
x
sumx = sum(x)

% II
[y, fvaly, exitflagy, outputy] = linprog(c, A, b, [],[],[0,0,0,0,0,0,0], []);
y
sumy = sum(y)
