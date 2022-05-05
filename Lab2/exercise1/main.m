%% def

c = [13 11].';
A = [4 5; 5 3; 1 2];
b = [1500 1575 420].';

cd = [1500 1575 420].';
Ad = (-1)*[4 5 1; 5 3 2];
bd = (-1)*[13 11].';

%% primal

[x] = linprog((-1)*c, A, b, [], [], [0,0,0], []); 
%optimal x1 = 270, x2 = 75

%% results 
c.' * x

A * x

%% dual


[y] = linprog(cd, Ad, bd, [], [], [0,0,0], []); 

%% results 
cd.' * y

Ad * y

%% Comparisson

(cd.' * y == c.' * x)

% SAME for both, which is expected


%% Sensitivity analysis

% shadow price explanation
% https://math.stackexchange.com/questions/91504/shadow-prices-in-linear-programming


c = [13 11].';
A = [4 5; 5 3; 1 2];
b = [1500 1575 420].';


[x, fval, exitflag, output, lambda] = linprog((-1)*c, A, b, [], [], [0,0], []); 
disp("shadow price")
fprintf("storage:\n %f\n", lambda.ineqlin(1))
fprintf("raw:\n %f\n", lambda.ineqlin(2))
fprintf("Prod. time:\n %f\n", lambda.ineqlin(3))


%% raw 

c = [13 11].';
A = [4 5; 5 3; 1 2];
b = [1500 1575 420].';


raws = [1575:15:1725];

N = length(raws);

fs = zeros(1, N);
shadow_price_raw = zeros(1, N);

for i=1:N
    b(2) = raws(i)
    [x, fval, exitflag, output, lambda] = linprog((-1)*c, A, b, [], [], [0,0], []); 
    fs(i) = c.' * x
    shadow_price_raw(i) = lambda.ineqlin(2);    
end

slopes = zeros(N, 1);

for i=1:N-1
    slopes(i) = (fs(i+1) - fs(i)) / (raws(i+1) - raws(i));
end

slopes(N) = slopes(N-1);

subplot(3,1,1);
grid on
hold on
plot(raws, fs, 'LineWidth', 3)
plot(raws, fs, 'o', 'LineWidth', 3)
xlabel("raw material")
ylabel("profit")
title("Sensitivity analysis of raw materials with shadow price 2.142857")

subplot(3,1,2);
grid on
hold on
plot(raws, slopes, 'LineWidth', 3)
plot(raws, slopes, 'o', 'LineWidth', 3)
xlabel("raw material")
ylabel("df/dx profit")
title("Derivative of profit")


subplot(3,1,3);
grid on
hold on
plot(raws, shadow_price_raw, 'LineWidth', 3)
plot(raws, shadow_price_raw, 'o', 'LineWidth', 3)
xlabel("raw material")
ylabel("shadow price")
title("Shadow price raw")









