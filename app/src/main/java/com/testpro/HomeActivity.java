package com.testpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.testpro.domain.Employee;
import com.testpro.domain.Manager;
import com.testpro.domain.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Rxjava Demo测试界面
 */
public class HomeActivity extends Activity {

    private TextView tv_1, tv_2;
    private Subscription s1;
    private Float[] fArray = {1.5f, 2f, 3.5f, 4f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click2();
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindClick();
            }
        });

        //测试取消绑定
        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("s1 == " + s1);
                if(s1 != null){
                    System.out.println("1 isUnsubscribed == "  + s1.isUnsubscribed());
                    s1.unsubscribe();
                    System.out.println("2 isUnsubscribed == "  + s1.isUnsubscribed());
                }
            }
        });
        findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("初始设置： 点击了tv_5----------");
            }
        });

        findViewById(R.id.tv_goto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(HomeActivity.this, SecondActivity.class));
            }
        });
    }

    private void bindClick() {
        //测试RxView组件绑定
        s1 = RxView.clicks(findViewById(R.id.tv_5))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void >() {
                    @Override
                    public void call(Void  f) {
                        System.out.println("设置绑定后：点击55555 ");
                    }
                });
    }

    private void click4() {
        //防止重复点击
        Observable.interval(0, 1500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long f) {
                        System.out.println("interval -first- call f = " + f);
                    }
                });
    }

    private void click3() {
        //测试基本的应用
        Observable.from(fArray)
                .scan(new Func2<Float, Float, Float>() {
                    @Override
                    public Float call(Float f1, Float f2) {
                        System.out.println("---scan1.call ");
                        return f1 + f2;
                    }
                }).takeLast(1).subscribe(new Action1<Float>() {
            @Override
            public void call(Float f) {
                System.out.println(" scan1.call --- f = " + f);
            }
        });

        Observable.from(fArray)
                .scan(new Func2<Float, Float, Float>() {
                    @Override
                    public Float call(Float f1, Float f2) {
                        System.out.println("---scan2.call");
                        return f1 + f2;
                    }
                }).elementAt(2).subscribe(new Action1<Float>() {
            @Override
            public void call(Float f) {
                System.out.println(" elementAt.call f = " + f);
            }
        });


        List<String> list = new ArrayList<String>();
        list.add("100");
        list.add("101");
        list.add("102");
        //测试集合的使用
        Observable<String> observable1 = Observable.merge(Observable.just("500", "1235", "99980"), Observable.from(list));
        s1 = observable1.map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return Integer.parseInt(s);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                System.out.println("click3 Action1 --- s = " + s);
            }
        });

        List<String> list2 = new ArrayList<String>();
        list2.add("qq");
        list2.add("q2");
        list2.add("a5");
        list2.add("g4");

        Observable.zip(Observable.from(list), Observable.from(list2), new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + "---" + s2;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("click3 zip.onCompleted");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
                System.out.println("click3 zip.onNext --- s = " + s);
            }
        });

    }

    private void click2() {
        Observable<String> testObservable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("Observable --- call");
                System.out.println("1 --- currentThread = " + Thread.currentThread().getName());
                subscriber.onNext("新--------的提示来了");
                subscriber.onNext("1新的提示来了");
                subscriber.onNext("22新的提示来了");
                subscriber.onNext("22新的提示来了");
                subscriber.onNext("22新的提示来了22");


                subscriber.onNext("测试中文啦~~~~");

                subscriber.onCompleted();
            }
        });

        testObservable2.subscribeOn(Schedulers.io())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        System.out.println(s + " filter1 thread = " + Thread.currentThread().getName());
                        return s.startsWith("22");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        System.out.println(s + " filter2 thread = " + Thread.currentThread().getName());
                        return s.endsWith("22");
                    }
                })
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("3 --- currentThread = " + Thread.currentThread().getName());
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError ---- e:  " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("2 --- currentThread = " + Thread.currentThread().getName());
                        tv_1.setText(s);
                        System.out.println("onNext --- s = " + s);
                    }
                });

    }

    private void testDomain() {
        Model m = new Model(new Employee("Employee5656", 15), new Manager("Manager-22", 16));
        testDomain1(m);
        m = new Model(new Manager("Manager1", 15), new Manager("Manager87989", 16));
        testDomain1(m);
    }

    private void testDomain1(Model<? extends Employee> m1) {
        System.out.println("First ***** getName: " + m1.getFirst());
        System.out.println("First getName: " + m1.getFirst().getName());
        System.out.println("Second getName: " + m1.getSecond().getName());
        System.out.println("---------------------------");
//        m1.setFirst(new Person("12121212", 12));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (s1 != null) {
            s1.unsubscribe();
        }

    }
}
