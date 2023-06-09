from flask import Flask,render_template,request

memory = list()

app=Flask(__name__)
@app.route('/',methods=['POST','GET'])
def home():
    if request.method =='GET':
        pass
    if request.method == 'POST':
        global memory
        memory = []
    return render_template('main.html')
@app.route('/result',methods=['POST','GET'])
def result():
    global memory
    if request.method =='POST':
        result=dict()
        result['name']=request.form.get('name')
        result['stnum']=request.form.get('stnum')
        result['major']=request.form.get('major')
        result['email']=request.form.get('email')+'@'+request.form.get('domain')
        result['gender']=request.form.get('gender')
        result['langs'] = ', '.join(request.form.getlist('lang'))
        memory.append(result)
        memory = sorted(memory, key=lambda result: result['stnum'])
    if request.method == 'GET':
        data = request.args.getlist('del')
        data.reverse()
        for i in data: memory.pop(int(i))
    return render_template('result.html',display=memory)


if __name__ =='__main__':
    app.run(debug=True)