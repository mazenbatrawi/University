module FA(a,b,cin,carn,sum,car);
input a,b,cin,carn;
output sum,car;
wire w1,w2,w3,w4;
xor(w1,b,cin);
xor(w2,a,w1);
xor(sum,carn,w2);
and(w3,a,w1);
and(w4,carn,w2);
or(car,w3,w4);
endmodule