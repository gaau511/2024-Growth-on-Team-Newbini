
const uploadBtn = document.getElementById('upload-btn');
const dropBox = document.getElementById('drop-box');
const fileInfo = document.querySelector('.file-info'); // 파일 정보 표시 영역
const uploadImage = document.getElementById('upload-image');
const fileUpload = document.getElementById('fileUpload');
const uploadedFilesDiv = document.querySelector('.uploaded-files'); // 업로드된 파일 이름을 표시할 영역
var filesArr = new Array();

uploadBtn.addEventListener('click', function() {
    upload();
});

// 이미지 클릭 시 파일 선택 창 열기
uploadImage.addEventListener('click', function() {
    fileUpload.click();
});

// 파일 선택 시 처리
fileUpload.addEventListener('change', function() {
    files = this.files;
    handleFiles(files);
});

function handleFiles(files) {
    console.log(files);
    // 파일 정보 표시
    let fileInfoText = '';
    if (files.length > 0) {
        fileInfoText = `파일이 선택되었습니다: <br>`;
        for (let i = 0; i < files.length; i++) {
        fileInfoText += `${files[i].name} (${files[i].size} 바이트) <br>`;
        }
    } else {
        fileInfoText = '파일을 선택하세요';
    }
    fileInfo.innerHTML = fileInfoText;


    // 업로드된 파일 이름 표시
    let uploadedFilesText = '업로드된 파일: <br>';
    for (let i = 0; i < files.length; i++) {
        uploadedFilesText += `${files[i].name} <br>`;
    }
    uploadedFilesDiv.innerHTML = uploadedFilesText;

    for (const file of files) {
        var reader = new FileReader();
            reader.onload = function () {
                filesArr.push(file);
            };
            reader.readAsDataURL(file);
    }
};

function upload() {
// 파일 업로드 예제 (서버로 전송)
    var formData = new FormData();
    for (var i = 0; i < filesArr.length; i++) {
        formData.append("attach_file", filesArr[i]);
    }

    fetch('/upload', { // 서버의 파일 업로드 엔드포인트로 변경해야 합니다.
        method: 'POST',
        body: formData
    })
    .then(response => {
      if (response.redirected) {
        window.location.href = response.url;
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });
}