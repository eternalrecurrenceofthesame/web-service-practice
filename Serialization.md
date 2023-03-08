## 직렬화

#### 스트림이란?
다양한 입출력 장치와 무관하게 일관성 있게 프로그램을 구현할 수 있도록 자바가 제공하는 일종의 가상 통로 

즉 자료를 출력 및 입력할 때 각각의 입출력 스트림을 구현해서 입력 출력을 할 수 있다.

키보드나 모니터 메모리 네트워크 등에 자료를 입력하고 거기서 자료를 다시 출력받을 때 사용하는 클래스

문자를 읽거나 파일을 입력하거나 출력하는 경우를 생각해보자 

#### 직렬화란?
클래스가 **인스턴스**화 되었을 때 변수 값은 계속 변하게됨. 그런데 **인스턴스**의 순간 상태를 그대로 저장하거나

네트웤을 통해 전송할 일이 있을 수 있음 이것을 직렬화 라고함. 반대의 경우 역직렬화라고 한다.

직렬화 과정에서 하는 일은 **인스턴스** 변수 값을 스트림으로 만드는 것이다.

ObjectInputStream, ObjectOutputStream 을 사용해서 **인스턴스**의 변수값을 스트림으로 만들어서 직렬화 하는 메커니즘!

```
class Person implements Serializable{ 
    private static final long serialVersionUID = -3141535L; //버전 관리 정보
    String name;
    String job;
    
    ...
    }
    
Person personW = new Person("웰스", "바보");
Person personC = new Person("웰스2", "고양이"); 

 try(FileOutputStream fos = new FileOutputStream("serial.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(personW);
            oos.writeObject(personC); // 직렬화, 역 직렬화는 inputStream을 사용!
        } catch(IOException e){
            e.printStackTrace();
        }

```

#### implements Serializable
직렬화는 인스턴스 내용이 외부로 유출되는 것이므로 프로그래머가 직접 직렬화 하겠다는 의도를

표시해야 한다.

#### transient 예약어
직렬화 대상이 되는 클래스는 모든 인스턴스의 변수가 직렬화되고 복원된다. 그런데 직렬화될 수 없는

클래스(Socket 클래스)가 인스턴스 변수로 있다거나 직렬화하고 싶지 않은 변수가 있을 수 있음 그럴 때 

transient 예약어를 필드에 넣어주자 그러면 안나옴

```
String name;
transient String job;
```

#### serialVersionUID 를 사용한 버전 관리
객체 역직렬화시 직렬화 할 때의 클래스와 상태가 다르면(수정, 변경) 오류가 발생한다.

직렬화는 자동으로 serialVersionUid 를 생성해서 저장하고 역직렬화시 버전이 맞지 않으면

오류가 발생, 그런데 작은 변경에도 클래스 버전이 계속 바뀐다면 네트워크로 객체를

공유해서 일하는 경우에 매번 클래스를 새로 배포해야하는 번거로움이 생김 

이런 경우 클래스의 버전관리를 직접할 수 있음 

직렬화의 대상이 되는 클래스 정보가 바뀌고 이를 공유해야 하는 경우에 버전 정보를 변경하면 된다!!
 
```
private static final long serialVersionUID = ;
```


### implements Externalizable 로 세밀한 직렬화 만들기
고양이의 이름만 입력 출력 하고 싶을 때 나이는 null 값으로 나옴.

```
@ToString
class Cat implements Externalizable{

String name;

String age;

public Cat() {}; // Externalizable 시 디폴트 생성자 있어야 한다. 복원시 호출됨

@Override
public void writeExternal(ObjectOutput out) throws IOException {
out.writeUTF(name); // 이름만 입력하고 싶을 때 
}
@Override
public void readExternal(ObjectInput int) throws IOException, ClassNotFoundException{
name = in.readUTF(); // 이름만 읽기
}



...

Cat cat = new Cat();
cat.name = "웰스";
cat.age = "1";

FileOutputStream fos = new FileOutputStream("external.out");
ObjectOutputStream oos = new ObjectOutputStream(fos);

try(fos; oos){
oos.writeObject(myCat);
}catch(IOException e){
e.printStackTrace();
}

FileInputStream fis = new FileInputStream("external.out");
ObjectInputStream ois = new ObjectInputStream(fis);

Cat cat = (Cat)ois.readObject();
System.out.println(cat);


}
```




