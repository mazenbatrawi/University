module mux4(d1,d2,d3,d4,s1,s0,outp);
input [3:0] d1,d2,d3,d4;
input s1,s0;
output reg [3:0]outp;

always @ (s1,s0)
begin
	if (s1==0)
		begin
		if (s0==0)
			outp=d1;
		else 
			outp = d2;
		end
	else
		begin
		if (s0==0)
			outp=d3;
		else
			outp=d4;
		end
end
endmodule