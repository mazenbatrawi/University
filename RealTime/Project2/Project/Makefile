CC = gcc
LDFLAGS = -lglut -lGLU -lGL -lm 

OBJECTS = parent sender receiver master_spy helpers spy encoders animation

all: 
	$(CC) parent.c -o parent -lpthread $(LDFLAGS)
	$(CC) sender.c -o sender
	$(CC) receiver.c -o receiver -lpthread
	$(CC) master_spy.c -o master_spy
	$(CC) helpers.c -o helpers -lpthread
	$(CC) spy.c -o spy -lpthread
	$(CC) encoders.c -o encoders -lpthread
	$(CC) animation.c -o animation $(LDFLAGS)
	ipcrm -a

clean:
	rm -f $(OBJECTS)
