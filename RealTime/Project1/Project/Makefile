CC = gcc
LDFLAGS = -lglut -lGLU -lGL -lm

OBJECTS = parent child animation

all: 
	$(CC) parent.c -o parent
	$(CC) child.c -o child
	$(CC) animation.c -o animation $(LDFLAGS)

clean:
	rm -f $(OBJECTS)
