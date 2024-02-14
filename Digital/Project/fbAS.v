module fbAS(a,b,cin,sum,car,v);
input [3:0] a,b;
output [3:0] sum;
input cin;
output car,v;
wire car1,car2,car3;
FA firstadder(a[0],b[0],cin,cin,sum[0],car1);
FA secondadder(a[1],b[1],cin,car1,sum[1],car2);
FA thirdadder(a[2],b[2],cin,car2,sum[2],car3);
FA fourthadder(a[3],b[3],cin,car3,sum[3],car);
xor (v,car3,car);
endmodule