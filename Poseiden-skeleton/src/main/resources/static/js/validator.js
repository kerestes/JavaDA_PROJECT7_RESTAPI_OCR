function doubleControl(id){
    inputText = document.getElementById(id).value;
    if(inputText.includes('.')){
        parts = inputText.split('.');
        parts[0] = parts[0].replace(/[^0-9]+/g, '');
        if(parts[1]){
            parts[1] = parts[1].replace(/[^0-9]+/g, '');
            document.getElementById(id).value = parts[0] + '.' + parts[1];
        } else {
            document.getElementById(id).value = parts[0] + '.';
        }
    } else {
        inputText = inputText.replace(/[^0-9]+/g, '');
        document.getElementById(id).value = inputText;
    }
}