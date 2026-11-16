package com.weekview.tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import java.text.SimpleDateFormat;
import java.util.*;

public final class MainActivity extends Activity {
    private CalendarView calendarView;
    private final android.os.Handler ticker = new android.os.Handler(android.os.Looper.getMainLooper());
    private final Runnable refresh = new Runnable() {
        @Override public void run() {
            if(calendarView != null)calendarView.invalidate();
            ticker.postDelayed(this, 1000L * 30);
        }
    };
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setFlags(1024, 1024);
        CalendarView view = new CalendarView();
        calendarView = view;
        if (state != null) {
            view.week.setTimeInMillis(state.getLong("week", view.week.getTimeInMillis()));
            view.day = state.getInt("day", 0);
            view.hour = state.getInt("hour", 9);
        }
        setContentView(view); view.requestFocus();
    }
    @Override protected void onResume() { super.onResume(); ticker.post(refresh); }
    @Override protected void onPause() { ticker.removeCallbacks(refresh); super.onPause(); }
    @Override public void onSaveInstanceState(Bundle state) {
        View root = findViewById(android.R.id.content);
        CalendarView view = (CalendarView)((android.view.ViewGroup)root).getChildAt(0);
        state.putLong("week", view.week.getTimeInMillis());
        state.putInt("day", view.day); state.putInt("hour", view.hour);
        super.onSaveInstanceState(state);
    }
    final class CalendarView extends View {
        final Paint p = new Paint(3);
        final Calendar week = Calendar.getInstance();
        int day, hour = 9, topHour = 7, toolbar = -1;
        float scale = 1, offsetX, offsetY;
        final int bg = Color.rgb(12,19,32), muted = Color.rgb(148,165,187);
        final int ink = Color.rgb(236,242,250), accent = Color.rgb(116,210,185);
        final String[] actions = {"‹  Previous", "Today", "Next  ›", "About"};
        CalendarView() {
            super(MainActivity.this);
            setFocusable(true); setFocusableInTouchMode(true);
            day = (week.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            week.add(Calendar.DAY_OF_MONTH, -day);
            week.set(Calendar.HOUR_OF_DAY, 0); week.set(Calendar.MINUTE, 0);
            week.set(Calendar.SECOND, 0); week.set(Calendar.MILLISECOND, 0);
            setContentDescription("Weekly calendar demo. Use arrow keys to select a day and hour. Press OK for details. Press up from midnight for week controls.");
        }
        Calendar date(int d) { Calendar c = (Calendar)week.clone(); c.add(Calendar.DAY_OF_MONTH,d); return c; }
        String fmt(Calendar c,String pattern) { return new SimpleDateFormat(pattern,Locale.getDefault()).format(c.getTime()); }
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold) {
            p.setColor(color); p.setTextSize(size); p.setTypeface(bold?Typeface.create("sans-serif",Typeface.BOLD):Typeface.create("sans-serif",Typeface.NORMAL)); c.drawText(s,x,y,p);
        }
        void box(Canvas c,float l,float t,float r,float b,int color,float radius) {
            p.setColor(color); p.setStyle(Paint.Style.FILL); c.drawRoundRect(l,t,r,b,radius,radius,p);
        }
        String event(int d,int h) {
            if (d==0 && h==9) return "Plan the week";
            if (d==1 && h==11) return "Coffee with Sam";
            if (d==2 && h==10) return "Project catch-up";
            if (d==3 && h==14) return "Appointment";
            if (d==4 && h==17) return "Dinner plans";
            if (d==5 && h==10) return "Weekend walk";
            if (d==6 && h==12) return "Family lunch";
            return null;
        }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c); c.drawColor(bg);
            scale = Math.min(getWidth()/1280f,getHeight()/720f);
            offsetX = (getWidth()-1280*scale)/2; offsetY = (getHeight()-720*scale)/2;
            c.save(); c.translate(offsetX,offsetY); c.scale(scale,scale);
            text(c,"Week on TV",48,62,29,ink,true);
            text(c,"Today · "+fmt(Calendar.getInstance(),"EEE, d MMM"),525,62,15,accent,true);
            text(c,fmt(week,"d MMM")+" – "+fmt(date(6),"d MMM yyyy"),48,101,22,muted,false);
            box(c,288,36,510,67,Color.rgb(34,54,62),15);
            text(c,"DEMO · SAMPLE EVENTS",302,57,14,accent,true);
            for(int i=0;i<4;i++) {
                float x = 680+i*139;
                box(c,x,39,x+130,83,toolbar==i?accent:Color.rgb(28,40,57),10);
                text(c,actions[i],x+15,67,17,toolbar==i?bg:ink,true);
            }
            float left=104, cw=160, top=211, rh=49;
            Calendar now = Calendar.getInstance();
            for(int d=0;d<7;d++) {
                Calendar dt=date(d); float x=left+d*cw;
                boolean today = fmt(dt,"yyyyMMdd").equals(fmt(now,"yyyyMMdd"));
                if(d==day && toolbar<0) box(c,x+2,123,x+cw-3,195,Color.rgb(28,47,62),12);
                text(c,fmt(dt,"EEE").toUpperCase(Locale.getDefault()),x+16,147,15,today?accent:muted,true);
                text(c,fmt(dt,"d"),x+16,181,29,today?accent:ink,true);
                p.setColor(Color.rgb(39,52,70)); p.setStrokeWidth(1); c.drawLine(x,top,x,top+8*rh,p);
            }
            for(int row=0;row<8;row++) {
                int h=topHour+row; float y=top+row*rh;
                text(c,String.format(Locale.getDefault(),"%02d:00",h),48,y+22,14,muted,false);
                p.setColor(Color.rgb(39,52,70)); c.drawLine(left,y,1224,y,p);
                for(int d=0;d<7;d++) {
                    String title=event(d,h); float x=left+d*cw;
                    if(title!=null) {
                        int color = d%2==0?Color.rgb(35,81,86):Color.rgb(61,65,111);
                        box(c,x+5,y+4,x+cw-5,y+rh-4,color,7);
                        c.save(); c.clipRect(x+12,y,x+cw-9,y+rh);
                        text(c,title,x+13,y+24,15,ink,true);
                        text(c,String.format(Locale.getDefault(),"%02d:00 – %02d:45",h,h),x+13,y+40,11,Color.rgb(196,215,227),false);
                        c.restore();
                    }
                }
            }
            if(toolbar<0) {
                float x=left+day*cw, y=top+(hour-topHour)*rh;
                p.setColor(accent); p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(3);
                c.drawRoundRect(x+3,y+2,x+cw-3,y+rh-2,8,8,p); p.setStyle(Paint.Style.FILL);
            }
            if(fmt(now,"yyyyMMdd").compareTo(fmt(week,"yyyyMMdd"))>=0 && fmt(now,"yyyyMMdd").compareTo(fmt(date(6),"yyyyMMdd"))<=0) {
                float nh=now.get(Calendar.HOUR_OF_DAY)+now.get(Calendar.MINUTE)/60f;
                if(nh>=topHour && nh<topHour+8) {
                    int nd=(now.get(Calendar.DAY_OF_WEEK)+5)%7;
                    float y=top+(nh-topHour)*rh, x=left+nd*cw;
                    p.setColor(Color.rgb(249,139,132)); p.setStrokeWidth(2); c.drawLine(x,y,x+cw,y,p); c.drawCircle(x,y,4,p);
                }
            }
            text(c,fmt(date(day),"EEEE, d MMMM")+" · "+String.format(Locale.getDefault(),"%02d:00",hour),48,644,18,ink,true);
            text(c,"↑ ↓ ← →  Move     OK  Details     ↑ from 00:00  Week controls     Menu  Week controls",48,681,15,muted,false);
            c.restore();
        }
        void reveal() { if(hour<topHour)topHour=hour; if(hour>topHour+7)topHour=hour-7; }
        void activate() {
            if(toolbar>=0) {
                if(toolbar==0)week.add(Calendar.DAY_OF_MONTH,-7);
                if(toolbar==2)week.add(Calendar.DAY_OF_MONTH,7);
                if(toolbar==1) {
                    Calendar now=Calendar.getInstance(); day=(now.get(Calendar.DAY_OF_WEEK)+5)%7;
                    week.setTimeInMillis(now.getTimeInMillis()); week.add(Calendar.DAY_OF_MONTH,-day);
                    week.set(Calendar.HOUR_OF_DAY,0); week.set(Calendar.MINUTE,0); week.set(Calendar.SECOND,0); week.set(Calendar.MILLISECOND,0);
                    hour=9; topHour=7; toolbar=-1;
                }
                if(toolbar==3)new AlertDialog.Builder(MainActivity.this).setTitle("Week on TV · Test build")
                    .setMessage("This prototype uses sample events. It does not connect to Google or collect account details.\n\nGoogle Calendar connection requires a registered Google OAuth application and will be added separately.\n\nRemote: arrows move between days and hours; OK opens details. Menu opens week controls. Back returns to the calendar, then exits.")
                    .setPositiveButton("Back to calendar",null).show();
            } else {
                String title=event(day,hour);
                new AlertDialog.Builder(MainActivity.this).setTitle(title==null?"No event at this time":title)
                    .setMessage(fmt(date(day),"EEEE, d MMMM yyyy")+"\n"+String.format(Locale.getDefault(),"%02d:00 – %02d:45",hour,hour)+"\n\n"+(title==null?"This time is free in the sample calendar.":"Sample event for testing the TV calendar.")+"\n\nThis demo does not create or edit real events.")
                    .setPositiveButton("Close",null).show();
            }
            invalidate();
        }
        @Override public boolean onKeyDown(int code,KeyEvent e) {
            switch(code) {
                case KeyEvent.KEYCODE_DPAD_LEFT: if(toolbar>=0)toolbar=Math.max(0,toolbar-1);else if(--day<0){day=6;week.add(Calendar.DAY_OF_MONTH,-7);} break;
                case KeyEvent.KEYCODE_DPAD_RIGHT: if(toolbar>=0)toolbar=Math.min(3,toolbar+1);else if(++day>6){day=0;week.add(Calendar.DAY_OF_MONTH,7);} break;
                case KeyEvent.KEYCODE_DPAD_UP: if(toolbar<0){if(hour==0)toolbar=1;else hour--;} break;
                case KeyEvent.KEYCODE_DPAD_DOWN: if(toolbar>=0)toolbar=-1;else hour=Math.min(23,hour+1); break;
                case KeyEvent.KEYCODE_MENU: toolbar=toolbar<0?1:-1; break;
                case KeyEvent.KEYCODE_BACK: if(toolbar>=0){toolbar=-1;break;} return super.onKeyDown(code,e);
                case KeyEvent.KEYCODE_DPAD_CENTER: case KeyEvent.KEYCODE_ENTER: if(e.getRepeatCount()==0)activate(); return true;
                default: return super.onKeyDown(code,e);
            }
            reveal();invalidate();return true;
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent e) {
            if(e.getAction()!=android.view.MotionEvent.ACTION_UP)return true;
            float x=(e.getX()-offsetX)/scale,y=(e.getY()-offsetY)/scale;
            if(y>=39&&y<=83&&x>=680&&x<1227){toolbar=Math.min(3,(int)((x-680)/139));activate();}
            else if(y>=211&&y<603&&x>=104&&x<1224){toolbar=-1;day=(int)((x-104)/160);hour=topHour+(int)((y-211)/49);activate();}
            invalidate();return true;
        }
    }
}
