#include <string>
#include <cstdlib>
#include <iostream>
#include <list>
#include <sys/time.h>
#include <math.h>
#define PI 3.14159265

/*
 * Header files for X functions
 */
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <unistd.h>

using namespace std;

#include "simon.h"

const int FPS = 60;
const int BufferSize = 10;
int tick = 0;
int tick_hl = 0;
double mytime = 1;
int atime = 0;

unsigned long now() {
    timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}

struct XInfo {
    Display*  display;
	Window   window;
	GC       gc[4];
    int screen;
    int width;
    int height;
    Simon *simon;
    int hl;
    int n;
};

class Displayable {
public:
	virtual void paint(XInfo& xinfo) = 0;
};



class Text : public Displayable {
public:


	virtual void paint(XInfo& xinfo) {
        string s;
        string c = to_string(xinfo.simon->getScore());
        switch (xinfo.simon->getState()) {
            // will only be in this state right after Simon object is contructed
            case Simon::START:
                s= "Press ENTER to play";
                break;
                // they won last round
                // score is increased by 1, sequence length is increased by 1
            case Simon::WIN:
                s = "You won! Press ENTER to continue.";
                break;
                // they lost last round
                // score is reset to 0, sequence length is reset to 1
            case Simon::LOSE:
                s = "You lose. Press ENTER to play again.";
                break;
            case Simon::COMPUTER:
                s = "Watch what I do ...";
                break;
            case Simon::HUMAN:
                s = "Your turn ...";
                break;
            default:
                // should never be any other state at this point ...
                break;
        }

        XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[1],
						  50, 100, s.c_str(), s.length() );
        XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[1],
                          50, 50, c.c_str(), c.length() );
	}

private:
};



class Circle : public Displayable {
public:
    virtual void paint(XInfo& xinfo) {

        resize(xinfo.width, xinfo.height);

        // draw circles
        int x = w / (n + 1);
        int y = h / 2;


        if((tick == 0 || atime == 1) && ((xinfo.simon->getState() == Simon::WIN) || (xinfo.simon->getState() ==Simon::LOSE) || (xinfo.simon->getState() ==Simon::START))) {
                    if (xinfo.hl == 0 && tick_hl == 0) {
                for (int j = 1; j <= n; ++j) {
                    int tem_y = oy + y - d / 2 + sin(j*PI/n+mytime*PI/100)  * 40;
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[3],
                             ox + x * j - d / 2, tem_y, d, d, 0, 360 * 64);

                    const char c = '0' + j;
                    XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                     4 + x * j, 4 + tem_y + d / 2, &c, 1);
                    ++mytime;
                }
                if(tick>0) --tick;
            } else if(tick_hl == 0) {
                    for (int j = 1; j <= n; ++j) {
                        int tem_y = oy + y - d / 2 + sin(j*PI/n+mytime*PI/100)  * 40;
                        if (j == xinfo.hl) {
                            XDrawArc(xinfo.display, xinfo.window, xinfo.gc[0],
                                     ox + x * j - d / 2, tem_y, d, d, 0, 360 * 64);
                        } else {
                            XDrawArc(xinfo.display, xinfo.window, xinfo.gc[3],
                                     ox + x * j - d / 2, tem_y, d, d, 0, 360 * 64);
                        }
                        const char c = '0' + j;
                        XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                         4 + x * j, 4 + tem_y + d / 2, &c, 1);
                        ++mytime;
                    }
                if(tick>0) --tick;
                } else {
                        for (int j = 1; j <= n; ++j) {
                            int tem_y0 = oy + y - d / 2 + sin(j*PI/n+mytime*PI/100)  * 40;
                    if (j == tick_hl) {
                        int diameter = d * tick / 30;
                        int tem_x = ox + x * j - d * tick / 60;
                        int tem_y = tem_y0 + d/2 - d * tick / 60;
                        XFillArc(xinfo.display, xinfo.window, xinfo.gc[1],
                                 ox + x * j - d / 2, tem_y0, d, d, 0, 360 * 64);
                        XDrawArc(xinfo.display, xinfo.window, xinfo.gc[2], tem_x, tem_y, diameter, diameter, 0, 360 * 64);
                        --tick;
                        if(tick == 0) {
                            tick_hl = 0;
                        }
                        ++mytime;
                        continue;
                    } else if (j == xinfo.hl) {
                        XDrawArc(xinfo.display, xinfo.window, xinfo.gc[0],
                                 ox + x * j - d / 2, tem_y0, d, d, 0, 360 * 64);
                        const char c = '0' + j;
                        XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                         4 + x * j, 4+tem_y0+d/2, &c, 1);
                        ++mytime;
                    } else {
                        XDrawArc(xinfo.display, xinfo.window, xinfo.gc[3],
                                 ox + x * j - d / 2, tem_y0, d, d, 0, 360 * 64);
                        const char c = '0' + j;
                        XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                         4 + x * j, 4+tem_y0+d/2, &c, 1);
                        ++mytime;
                    }

                }
                        if(tick == 0) atime = 0;
            }

        } else {


        if (xinfo.hl == 0 && tick_hl == 0) {
            for (int j = 1; j <= n; ++j) {
                XDrawArc(xinfo.display, xinfo.window, xinfo.gc[1],
                         ox + x * j - d / 2, oy + y - d / 2, d, d, 0, 360 * 64);
                const char c = '0' + j;

                XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                 4 + x * j, 14 + h / 2, &c, 1);
            }
            if (tick > 0) --tick;
        } else if (tick_hl == 0) {
            for (int j = 1; j <= n; ++j) {
                if (j == xinfo.hl) {
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[0],
                             ox + x * j - d / 2, oy + h / 2 - d / 2, d, d, 0, 360 * 64);
                } else {
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[1],
                             ox + x * j - d / 2, oy + h / 2 - d / 2, d, d, 0, 360 * 64);
                }
                const char c = '0' + j;
                XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                 4 + x * j, 14 + h / 2, &c, 1);
            }
            if (tick>0) --tick;
        } else {
            for (int j = 1; j <= n; ++j) {
                if (j == tick_hl) {
                    int diameter = d * tick / 30;
                    int tem_x = ox + x * j - d * tick / 60;
                    int tem_y = oy + h / 2 - d * tick / 60;
                    XFillArc(xinfo.display, xinfo.window, xinfo.gc[1],
                             ox + x * j - d / 2, oy + y - d / 2, d, d, 0, 360 * 64);
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[2], tem_x, tem_y, diameter, diameter, 0, 360 * 64);
                    --tick;
                    if (tick == 0) {
                        tick_hl = 0;
                        tick = 30;
                    }
                    continue;
                } else if (j == xinfo.hl) {
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[0],
                             ox + x * j - d / 2, oy + h / 2 - d / 2, d, d, 0, 360 * 64);
                    const char c = '0' + j;
                    XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                     4 + x * j, 14 + h / 2, &c, 1);
                } else {
                    XDrawArc(xinfo.display, xinfo.window, xinfo.gc[1],
                             ox + x * j - d / 2, oy + h / 2 - d / 2, d, d, 0, 360 * 64);
                    const char c = '0' + j;
                    XDrawImageString(xinfo.display, xinfo.window, xinfo.gc[1],
                                     4 + x * j, 14 + h / 2, &c, 1);
                }
            }
        }
        }
    }

    void resize(int w, int h) {
        this->w = w;
        this->h = h;
        return;
    }

    // constructor
    Circle(int ox, int oy, int w, int h, int d, int n): ox(ox), oy(oy), w(w), h(h), d(d), n(n) {}
private:
    int ox;
    int oy;
    int w; //max width
    int h; //max height
    int d; // diameter
    int n;
};



void initX(int argc, char* argv[], XInfo& xinfo, Simon &simon) {
    XSizeHints hints;
    hints.x = 10;
    hints.y = 10;
    hints.width = 800;
    hints.height = 400;
    hints.flags = PPosition | PSize;

    xinfo.display = XOpenDisplay( "" );
    if ( !xinfo.display ) {
        cerr << "Can't open display." << endl;
    }
    xinfo.screen = DefaultScreen( xinfo.display );

    unsigned long white, black;
    white = XWhitePixel( xinfo.display, xinfo.screen );
    black = XBlackPixel( xinfo.display, xinfo.screen );
    xinfo.window = XCreateSimpleWindow(
            xinfo.display,       // display where window appears
            DefaultRootWindow( xinfo.display ), // window's parent in window tree
            hints.x, hints.y,                  // upper left corner location
            hints.width, hints.height,                  // size of the window
            5,               // width of window's border
            black,           // window border colour
            white );             // window background colour
    // extra window properties like a window title
    XSetStandardProperties(
            xinfo.display,    // display containing the window
            xinfo.window,   // window whose properties are set
            "a1",  // window's title
            "a1",       // icon's title
            None,       // pixmap for the icon
            argv, argc,     // applications command line args
            &hints );         // size hints for the window


    int i = 0;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i] , BlackPixel(xinfo.display, xinfo.screen));
    XSetBackground(xinfo.display, xinfo.gc[i] , WhitePixel(xinfo.display, xinfo.screen));
    XSetFillStyle(xinfo.display,  xinfo.gc[i] , FillSolid);
    XSetLineAttributes(xinfo.display, xinfo.gc[i] ,
                       4,       // 4 is line width
                       LineSolid, CapButt, JoinRound);// other line options
    XFontStruct * font;
    font = XLoadQueryFont (xinfo.display, "*x24");
    XSetFont (xinfo.display, xinfo.gc[i] , font->fid);

    i = 1;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i] , BlackPixel(xinfo.display, xinfo.screen));
    XSetBackground(xinfo.display, xinfo.gc[i] , WhitePixel(xinfo.display, xinfo.screen));
    XSetLineAttributes(xinfo.display, xinfo.gc[i] ,
                       1,       // 1 is line width
                       LineSolid, CapButt, JoinRound);
    font = XLoadQueryFont (xinfo.display, "*x24");
    XSetFont (xinfo.display, xinfo.gc[i] , font->fid);

    i = 2;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i] , WhitePixel(xinfo.display, xinfo.screen));
    XSetLineAttributes(xinfo.display, xinfo.gc[i] ,
                       1,       // 1 is line width
                       LineSolid, CapButt, JoinRound);

    i = 3;
    XColor xcolour;
    xcolour.red = 32000; xcolour.green = 65000; xcolour.blue = 32000;
    xcolour.flags = DoRed | DoGreen | DoBlue;
    XAllocColor(xinfo.display, DefaultColormap(xinfo.display, 0 ), &xcolour);
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i] , xcolour.pixel);
    XSetLineAttributes(xinfo.display, xinfo.gc[i] ,
                       1,       // 1 is line width
                       LineSolid, CapButt, JoinRound);
    font = XLoadQueryFont (xinfo.display, "*x24");
    XSetFont (xinfo.display, xinfo.gc[i] , font->fid);


    XSelectInput(xinfo.display, xinfo.window,
                 ButtonPressMask | KeyPressMask |
                 PointerMotionMask |
                 StructureNotifyMask);

    xinfo.simon = &simon;
    xinfo.hl = 0;

    /*
     * Put the window on the screen.
     */
    XMapRaised( xinfo.display, xinfo.window );
    XFlush(xinfo.display);
    // give server time to setup
    sleep(1);
}

void repaint( list<Displayable*> &dList, XInfo& xinfo) {
    list<Displayable*>::const_iterator begin = dList.begin();
    list<Displayable*>::const_iterator end = dList.end();
    XClearWindow(xinfo.display, xinfo.window);

    while ( begin != end ) {
        Displayable* d = *begin;
        d->paint(xinfo);
        begin++;
    }
    XFlush(xinfo.display);
}

void handleResize(XInfo &xinfo, XEvent &event) {
    XConfigureEvent xce = event.xconfigure;

    if (xce.width != xinfo.width || xce.height != xinfo.height) {
        xinfo.width = xce.width;
        xinfo.height = xce.height;
    }
}


void handleMotion(XInfo &xinfo, XEvent &event) {
    int x = xinfo.width / (xinfo.n + 1);
    int y = xinfo.height / 2;
    int distance;
    int o_x;
    int o_y;

    for(int j = 1; j <= xinfo.n; ++j) {
        o_x = event.xbutton.x - 10 - x * j;
        o_y = event.xbutton.y - 10 - y;
        distance = o_x * o_x + o_y * o_y;
        if (distance <= 2500) {
            xinfo.hl = j;
            return;
        }
    }
    xinfo.hl = 0;
    return;
}


void handleButtonPress(XInfo &xinfo, XEvent &event) {
    if(xinfo.hl != 0) {
        tick = 30;
        tick_hl = xinfo.hl;
        if((xinfo.simon->getState() == Simon::START) || (xinfo.simon->getState() == Simon::WIN) || (xinfo.simon->getState() == Simon::LOSE)) {atime = 1;}
        if(xinfo.simon->getState() == Simon::HUMAN) {xinfo.simon->verifyButton(xinfo.hl - 1);}
    }

}


void handleKeyPress(XInfo &xinfo, XEvent &event) {
    KeySym key;
    char text[BufferSize];

    int i = XLookupString(
            (XKeyEvent *)&event, 	// the keyboard event
            text, 					// buffer when text will be written
            BufferSize, 			// size of the text buffer
            &key, 					// workstation-independent key symbol
            NULL );					// pointer to a composeStatus structure (unused)
    if ( i == 1) {
        if (text[0] == 'q') {
            XCloseDisplay(xinfo.display);
            cerr << "Terminating normally.";
        }
        if (text[0] == 32 && ((xinfo.simon->getState() == Simon::WIN) || (xinfo.simon->getState() ==Simon::LOSE) || (xinfo.simon->getState() ==Simon::START))) {
            xinfo.simon->newRound();
        }
    }
}




void eventLoop(list<Displayable*> &dList, XInfo &xinfo) {


    XEvent event;
    unsigned long lastRepaint = 0;
    while( true ) {

        if (XPending(xinfo.display) > 0) {
            // blocking!
            XNextEvent( xinfo.display, &event );
            cout << "event.type=" << event.type << "\n";
            switch( event.type ) {
                case ButtonPress:
                    handleButtonPress(xinfo, event);
                    continue;
                case KeyPress:
                    handleKeyPress(xinfo, event);
                    continue;
                case MotionNotify:
                    handleMotion(xinfo, event);
                    continue;
                case ConfigureNotify:
                    handleResize(xinfo, event);
                    continue;
            }
        }

        if(xinfo.simon->getState() == Simon::COMPUTER && tick == 0) {
            tick = 30;
            tick_hl = xinfo.simon->nextButton()+1;
        }


        unsigned long end = now();
        if (end - lastRepaint > 1000000 / FPS) {
            repaint(dList, xinfo);
            lastRepaint = now();
        }
    }
}


int main ( int argc, char* argv[] ) {

	// get the number of buttons from args
	// (default to 4 if no args)
	int n = 4;
    if (argc > 1) {
        n = atoi(argv[1]);
    }
    n = max(1, min(n, 9));

    // create the Simon game object
	Simon simon = Simon(n, true);

	cout << "Playing with " << simon.getNumButtons() << " buttons." << endl;

    XInfo xinfo;
    xinfo.n = n;

    initX(argc, argv, xinfo, simon);

    list<Displayable*> dList;

    dList.push_back(new Text());
    dList.push_back(new Circle(10, 10, 800, 400, 100, n));



    eventLoop(dList, xinfo);

    XCloseDisplay(xinfo.display);

}
