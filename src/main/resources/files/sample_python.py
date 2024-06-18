import json
import copy
import csv


with open("sample.json", "r", encoding='UTF8') as f:
	datas = json.load(f)
data = datas['result']
start_check = "금액"
finish_check = "합계"
receipt_size = data[0]['location']['y'] - data[len(data)-1]['location']['y']
print("receipt size :" ,receipt_size)
print("\n")
validate_range = round(receipt_size * 0.025)
print("validate_range :" , validate_range)
print("\n")

print("*******************raw json to dict**********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")

for entry in data :
    entry['location']['x'] = round(entry['location']['x'],3)
    entry['location']['y'] = round(entry['location']['y'],3)
    entry['size']['width'] = round(entry['size']['width'],3)
    entry['size']['height'] = round(entry['size']['height'],3)

data = sorted(data, key=lambda x: x['location']['y'], reverse=True)

print("*******************simplization**********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")

	
i = 0
for i in range(0, len(data)):
    if data[i]['type'] == 'text':
        if start_check in data[i]['text']:
            data = data[i:]
            break


print("*******************after find start point**********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")
	
start_y=data[0]['location']['y']
for j in range(0, len(data)-i):
    target = data[j]['location']['y']
    if target > start_y - validate_range :
        pass
    else:
        data = data[j:]
        break

print("*******************start point와 같은 줄에 있는 제외**********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")

for entry in data :
    if entry['type'] == 'number' :
        test = entry['text'].replace(',', '').replace('원','').replace(' ','')
        if test.isdigit() == False:
            entry['type'] = 'text'
        else:
            entry['text'] = test
    
    if entry['type'] == 'text' :
        new_text = ""
        for i in range(0, len(entry['text'])) :
            if '\uac00' <= entry['text'][i] <= '\ud7a3':  
                entry['text'] = entry['text'][i:]    
                break

print("*******************data 후처리**********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")

i = 0
while i < len(data) :
    if data[i]['type'] == 'text' :
        j = i+1
        while j < len(data) :
            if data[j]['location']['y'] > data[i]['location']['y'] - validate_range and data[j]['type'] == 'text':
                data[i]['text'] += data[j]['text']
                del(data[j])
            else :
                break
    i+=1
        


print("*******************string 합치기*********************\n")
print(data)
print("\n\n\n\n\n\n\n\n\n")

i = 0
for i in range(0, len(data)):
    if data[i]['type'] == 'text':
        if finish_check in data[i]['text']:
            data = data[:i+2]
            break



#block scope 찾기
first_text = 0
second_text = 0
for entry in data :
    if entry['type'] == 'text' and first_text == 0 :
        first_text = entry['location']['y']
    elif entry['type'] == 'text' :
        second_text = entry['location']['y']
        break
    else:
        pass

block_scope = first_text - second_text


row_data = []
start_point = data[0]
avg_height = start_point['size']['height']
target_y  = start_point['location']['y']
temp = []
for entry in data :
    if entry['location']['y'] > target_y - validate_range and entry['location']['y'] < target_y + validate_range :
        temp.append(entry)
    elif entry['location']['y'] < target_y - avg_height + validate_range and  entry['location']['y'] > target_y - avg_height - validate_range :
        row_data.append(copy.deepcopy(temp))
        target_y = entry['location']['y']
        temp = []
        temp.append(entry)
    else:
        target_y = entry['location']['y']
        row_data.append(copy.deepcopy(temp))
        row_data.append([])
        temp = []
    
if len(temp) != 0 :
    row_data.append(copy.deepcopy(temp))

print("*******************get data by line*****************")
print(row_data)
print("\n\n\n\n\n\n\n\n\n\n")

j = 0
while j < len(row_data) - 2 :
    if row_data[j][0]['location']['y'] - row_data[j+1][0]['location']['y'] < block_scope - validate_range:
        row_data[j] += row_data[j+1]
        del(row_data[j+1])
    else:
        j+=1

print("*******************Apply scope rule*****************")
print(row_data)
print("\n\n\n\n\n\n\n\n\n\n")

print(row_data[8])


if len(row_data[0]) == 1 :
    pattern = ["상품"]
elif len(row_data[0]) == 2 :
    pattern = ["상품", "금액"]
elif len(row_data[0]) == 3 :
    pattern = ["상품", "수량", "금액"]
elif len(row_data[0]) == 4 :
    pattern = ["상품", "단가", "수량", "금액"]

##############open csv file###############

# CSV 파일명을 지정
csv_filename = "result2.csv"

# CSV 파일을 쓰기 모드로 열기
with open(csv_filename, mode='w', newline='') as csv_file:
    csv_writer = csv.writer(csv_file)
    csv_writer.writerow(pattern)
    for entry in row_data :
        entry = sorted(entry, key=lambda x: x['location']['x'])
        if len(entry) != len(row_data[0]) :
            #합계 발견
            if any(item['type'] == 'text' and item['text'] == finish_check for item in entry) :
                csv_writer.writerow([item['text'] for item in entry if item['type'] == 'number' or item['text'] == finish_check])
                break
            else :
                pass
        else :
            csv_writer.writerow([item['text'] for item in entry])

